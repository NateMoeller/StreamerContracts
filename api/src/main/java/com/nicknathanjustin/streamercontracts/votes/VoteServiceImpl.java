package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
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
    public void recordVote(@NonNull final UserModel voter,
                           @NonNull final ContractModel contractModel,
                           @NonNull final boolean flaggedCompleted) {
        validateContract(contractModel);

        if (userCanVoteOnContract(voter, contractModel)) {
            final VoteModel voteModel = VoteModel.builder()
                    .voter(voter)
                    .contract(contractModel)
                    .viewerFlaggedComplete(flaggedCompleted)
                    .votedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            voteModelRepository.save(voteModel);
        }
    }

    @Override
    public boolean isVotingComplete(@NonNull final ContractModel contractModel) {
        validateContract(contractModel);

        final UUID contractId = contractModel.getId();
        final Optional<VoteModel> optionalProposerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getProposer().getId());
        final Optional<VoteModel> optionalStreamerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getStreamer().getId());
        final boolean proposerAndStreamerHaveVoted = optionalProposerVote.isPresent() && optionalStreamerVote.isPresent();
        return proposerAndStreamerHaveVoted ||
               proposerMarkedContractCompleted(contractModel) ||
               streamerMarkedContractFailed(contractModel) ||
               isContractExpired(contractModel);
    }

    @Override
    public VoteOutcome getVoteOutcome(@NonNull final ContractModel contractModel) {
        if (!isVotingComplete(contractModel)) {
            throw new IllegalArgumentException("Attempted to check if contract was completed before voting was finished. ContractId: " + contractModel.getId());
        }

        VoteOutcome voteOutcome = VoteOutcome.DISPUTE;
        final UUID contractId = contractModel.getId();
        final Optional<VoteModel> optionalProposerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getProposer().getId());
        final Optional<VoteModel> optionalStreamerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getStreamer().getId());
        if (proposerMarkedContractCompleted(contractModel)) {
            log.info("Proposer has marked contract: {} as completed", contractModel.getId());
            voteOutcome = VoteOutcome.COMPLETED;
        } else if (streamerMarkedContractFailed(contractModel)) {
            log.info("Streamer has marked contract: {} as failed", contractModel.getId());
            voteOutcome = VoteOutcome.FAILED;
        } else if(!optionalProposerVote.isPresent() && !optionalStreamerVote.isPresent()) {
            log.info("Neither Streamer nor Proposer voted on contract: {}. contractModel.isAccepted(): {}",
                    contractModel.getId(),
                    contractModel.isAccepted());
            voteOutcome = contractModel.isAccepted() ? VoteOutcome.COMPLETED : VoteOutcome.FAILED;
        } else if(!optionalStreamerVote.isPresent()) {
            log.info("Streamer did not vote on contract: {} and proposer did not mark contract complete. ProposerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    optionalProposerVote.get().isViewerFlaggedComplete());
            voteOutcome = VoteOutcome.FAILED;
        } else if(!optionalProposerVote.isPresent()){
            log.info("Proposer did not vote on contract: {} and streamer did not mark contract failed. StreamerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    optionalStreamerVote.get().isViewerFlaggedComplete());
            voteOutcome = VoteOutcome.COMPLETED;
        } else {
            log.info("Disputed outcome detected when completing contract: {}", contractModel.getId());
        }
        
        log.info("Contract: {} had the following voteOutcome: {}", contractModel.getId(), voteOutcome);
        return voteOutcome;
    }

    /**
     * Proposer marking a contract complete is a special case where funds should be released to the streamer
     * without considering additional votes. This is done because the proposer is the one releasing the money and if
     * they want to give the money to the streamer we will let them.
     */
    private boolean proposerMarkedContractCompleted(@NonNull final ContractModel contractModel) {
        final Optional<VoteModel> optionalProposerVote = voteModelRepository.findByContractIdAndVoterId(contractModel.getId(), contractModel.getProposer().getId());
        return optionalProposerVote.isPresent() && optionalProposerVote.get().isViewerFlaggedComplete();
    }

    /**
     * Streamer marking a contract failed is a special case where funds should be returned to the proposer
     * without considering additional votes. The streamer has every incentive to mark a contract as complete, so if
     * they mark it as failed we trust them without considering other votes.
     */
    private boolean streamerMarkedContractFailed(@NonNull final ContractModel contractModel) {
        final Optional<VoteModel> optionalStreamerVote = voteModelRepository.findByContractIdAndVoterId(contractModel.getId(), contractModel.getStreamer().getId());
        return optionalStreamerVote.isPresent() && !optionalStreamerVote.get().isViewerFlaggedComplete();
    }

    private boolean userCanVoteOnContract(@NonNull final UserModel voter, @NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();

        final UUID proposerId = contractModel.getProposer().getId();
        final boolean isVoterContractProposer = voter.getId().equals(proposerId);
        final boolean proposerHasVoted = (voteModelRepository.findByContractIdAndVoterId(contractId, proposerId).isPresent());

        final UUID streamerId = contractModel.getStreamer().getId();
        final boolean isVoterContractStreamer = voter.getId().equals(streamerId);
        final boolean streamerHasVoted = (voteModelRepository.findByContractIdAndVoterId(contractId, streamerId).isPresent());

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

    private void validateContract(@NonNull final ContractModel contractModel) {
        if (contractModel.isCommunityContract()) {
            throw new IllegalArgumentException("Voting logic not yet implemented for community contracts. ContractId " + contractModel.getId());
        }

        if (!contractModel.isAccepted() && !isContractExpired(contractModel)) {
            throw new IllegalArgumentException("Cannot vote on an unaccepted contract that hasnt expired yet. ContractId " + contractModel.getId());
        }

        if (contractModel.getIsCompleted() != null) {
            throw new IllegalArgumentException("Cannot vote on a completed contract. ContractId " + contractModel.getId());
        }

        //TODO: add check for isDeclined and isExpired
    }

    private boolean isContractExpired(@NonNull final ContractModel contractModel) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(contractModel.getExpiresAt());
    }
}
