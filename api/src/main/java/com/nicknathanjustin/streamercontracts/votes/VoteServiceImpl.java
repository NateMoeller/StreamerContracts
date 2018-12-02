package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService{

    @NonNull final VoteModelRepository voteModelRepository;

    @Override
    public boolean recordVote(@NonNull final UserModel voter,
                           @NonNull final ContractModel contractModel,
                           final boolean flaggedCompleted) {
        if (!isContractVotable(contractModel)) {
            return false;
        }

        if (userCanVoteOnContract(voter, contractModel)) {
            final VoteModel voteModel = VoteModel.builder()
                    .voter(voter)
                    .contract(contractModel)
                    .viewerFlaggedComplete(flaggedCompleted)
                    .votedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            voteModelRepository.save(voteModel);
            return true;
        }

        return false;
    }

    @Override
    public boolean isVotingComplete(@NonNull final Optional<VoteModel> optionalProposerVote,
                                    @NonNull final Optional<VoteModel> optionalStreamerVote,
                                    @NonNull final ContractModel contractModel) {
        if (!isContractSettleable(contractModel)) {
            return false;
        }

        final boolean proposerAndStreamerHaveVoted = optionalProposerVote.isPresent() && optionalStreamerVote.isPresent();
        return proposerAndStreamerHaveVoted ||
               proposerMarkedContractCompleted(optionalProposerVote) ||
               streamerMarkedContractFailed(optionalStreamerVote) ||
               isContractPastSettleTimestamp(contractModel);
    }

    @Override
    public ContractState getVoteOutcome(@NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();
        final Optional<VoteModel> optionalProposerVote = getVoteByContractIdAndVoterId(contractId, contractModel.getProposer().getId());
        final Optional<VoteModel> optionalStreamerVote = getVoteByContractIdAndVoterId(contractId, contractModel.getStreamer().getId());

        if (!isVotingComplete(optionalProposerVote, optionalStreamerVote, contractModel)) {
            throw new IllegalStateException("Attempted to check if contract was completed before voting was finished. ContractId: " + contractModel.getId());
        }

        ContractState voteOutcome = ContractState.DISPUTED;
        if (proposerMarkedContractCompleted(optionalProposerVote)) {
            log.info("Proposer has marked contract: {} as completed", contractId);
            voteOutcome = ContractState.COMPLETED;
        } else if (streamerMarkedContractFailed(optionalStreamerVote)) {
            log.info("Streamer has marked contract: {} as failed", contractId);
            voteOutcome = ContractState.FAILED;
        } else if(!optionalProposerVote.isPresent() && !optionalStreamerVote.isPresent()) {
            // If we get here we have already validated that the contract is in the ACCEPTED state and that the contract needs to be settled.
            // If there are no votes on the contract, we will default to paying the streamer.
            log.info("Neither Streamer nor Proposer voted on contract: {}. contractModel.getState(): {}",
                    contractId,
                    contractModel.getState());
            voteOutcome = ContractState.COMPLETED;
        } else if(!optionalStreamerVote.isPresent()) {
            log.info("Streamer did not vote on contract: {} and proposer did not mark contract complete. ProposerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    optionalProposerVote.get().isViewerFlaggedComplete());
            voteOutcome = ContractState.FAILED;
        } else if(!optionalProposerVote.isPresent()){
            log.info("Proposer did not vote on contract: {} and streamer did not mark contract failed. StreamerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    optionalStreamerVote.get().isViewerFlaggedComplete());
            voteOutcome = ContractState.COMPLETED;
        } else {
            log.info("Disputed outcome detected when completing contract: {}", contractId);
        }
        
        log.info("Contract: {} had the following voteOutcome: {}", contractId, voteOutcome);
        return voteOutcome;
    }

    @Override
    public Optional<VoteModel> getVoteByContractIdAndVoterId(@NonNull final UUID contractId, @NonNull final UUID userId) {
        return voteModelRepository.findByContractIdAndVoterId(contractId, userId);
    }

    /**
     * Proposer marking a contract complete is a special case where funds should be released to the streamer
     * without considering additional votes. This is done because the proposer is the one releasing the money and if
     * they want to give the money to the streamer we will let them.
     */
    private boolean proposerMarkedContractCompleted(@NonNull final Optional<VoteModel> optionalProposerVote) {
        return optionalProposerVote.isPresent() && optionalProposerVote.get().isViewerFlaggedComplete();
    }

    /**
     * Streamer marking a contract failed is a special case where funds should be returned to the proposer
     * without considering additional votes. The streamer has every incentive to mark a contract as complete, so if
     * they mark it as failed we trust them without considering other votes.
     */
    private boolean streamerMarkedContractFailed(@NonNull final Optional<VoteModel> optionalStreamerVote) {
        return optionalStreamerVote.isPresent() && !optionalStreamerVote.get().isViewerFlaggedComplete();
    }

    private boolean userCanVoteOnContract(@NonNull final UserModel voter, @NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();

        final UUID proposerId = contractModel.getProposer().getId();
        final boolean isVoterContractProposer = voter.getId().equals(proposerId);
        final boolean proposerHasVoted = getVoteByContractIdAndVoterId(contractId, proposerId).isPresent();

        final UUID streamerId = contractModel.getStreamer().getId();
        final boolean isVoterContractStreamer = voter.getId().equals(streamerId);
        final boolean streamerHasVoted = getVoteByContractIdAndVoterId(contractId, streamerId).isPresent();

        final boolean isUserAllowedToVote = isVoterContractProposer || isVoterContractStreamer;
        if (!isUserAllowedToVote) {
            log.warn("Unauthorized vote detected. User: {} attempted to vote on contractId: {} without having permissions to do so",
                    voter.getId(),
                    contractModel.getId());

            return false;
        }

        final boolean isProposerAndHasVoted = isVoterContractProposer && proposerHasVoted;
        final boolean isStreamerAndHasVoted = isVoterContractStreamer && streamerHasVoted;
        final boolean userHasVoted = isProposerAndHasVoted || isStreamerAndHasVoted;
        if(userHasVoted) {
            log.warn("Multiple votes detected. User: {} attempted to vote on contractId: {} more than once",
                    voter.getId(),
                    contractModel.getId());

            return false;
        }

        return true;
    }

    private boolean isContractVotable(@NonNull final ContractModel contractModel) {
        UUID contractId = contractModel.getId();
        if (!contractModel.getState().equals(ContractState.ACCEPTED.name())) {
            log.warn(String.format("Cannot vote on a contract that has not been accepted. Contract Id: %s", contractId));
            return false;
        }

        if (isContractPastSettleTimestamp(contractModel)) {
            log.warn(String.format("Cannot vote on a contract that is past the settlement timestamp. Contract Id: %s Settles At: %s", contractId, contractModel.getSettlesAt().toString()));
            return false;
        }

        return true;
    }

    private boolean isContractSettleable(@NonNull final ContractModel contractModel) {
        UUID contractId = contractModel.getId();
        if (contractModel.isCommunityContract()) {
            log.warn(String.format("Cannot settle a community contract because the logic has not yet been implemented. Contract Id: %s", contractId));
            return false;
        }

        if (!contractModel.getState().equals(ContractState.ACCEPTED.name())) {
            log.warn(String.format("Cannot settle a contract that has not been accepted. Contract Id: %s", contractId));
            return false;
        }

        return true;
    }

    private boolean isContractPastSettleTimestamp(@NonNull final ContractModel contractModel) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(contractModel.getSettlesAt());
    }
}
