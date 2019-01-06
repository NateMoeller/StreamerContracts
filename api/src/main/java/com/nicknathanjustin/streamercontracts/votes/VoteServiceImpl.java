package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

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
                           final boolean flaggedCompleted) {
    	if (isContractPastSettleTimestamp(contractModel)) {
            throw new IllegalStateException(String.format("Cannot vote on a contract that is past the settlement timestamp. Contract Id: %s Settles At: %s", contractModel.getId(), contractModel.getSettlesAt().toString()));
        }
        assertContractIsSettleable(contractModel);
        assertUserCanVoteOnContract(voter, contractModel);

        final VoteModel voteModel = VoteModel.builder()
                    .voter(voter)
                    .contract(contractModel)
                    .viewerFlaggedComplete(flaggedCompleted)
                    .votedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
        voteModelRepository.save(voteModel);
    }

    @Override
    public boolean isVotingComplete(@Nullable final VoteModel proposerVote,
                                    @Nullable final VoteModel streamerVote,
                                    @NonNull final ContractModel contractModel) {
        assertContractIsSettleable(contractModel);

        final boolean proposerAndStreamerHaveVoted = proposerVote != null && streamerVote != null;
        return proposerAndStreamerHaveVoted ||
               proposerMarkedContractCompleted(proposerVote) ||
               streamerMarkedContractFailed(streamerVote) ||
               isContractPastSettleTimestamp(contractModel);
    }

    @Override
    public ContractState getVoteOutcome(@Nullable final VoteModel proposerVote,
                                        @Nullable final VoteModel streamerVote,
                                        @NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();

        if (!isVotingComplete(proposerVote, streamerVote, contractModel)) {
            throw new IllegalStateException("Attempted to check if contract was completed before voting was finished. ContractId: " + contractModel.getId());
        }

        ContractState voteOutcome = ContractState.DISPUTED;
        if (proposerMarkedContractCompleted(proposerVote)) {
            log.info("Proposer has marked contract: {} as completed", contractId);
            voteOutcome = ContractState.COMPLETED;
        } else if (streamerMarkedContractFailed(streamerVote)) {
            log.info("Streamer has marked contract: {} as failed", contractId);
            voteOutcome = ContractState.FAILED;
        } else if(proposerVote == null && streamerVote == null) {
            log.info("Neither Streamer nor Proposer voted on contract: {}. contractModel.getState(): {}",
                    contractId,
                    contractModel.getState());
            voteOutcome = ContractState.EXPIRED;
        } else if(streamerVote == null) {
            log.info("Streamer did not vote on contract: {} and proposer did not mark contract complete. ProposerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    proposerVote.isViewerFlaggedComplete());
            voteOutcome = ContractState.FAILED;
        } else if(proposerVote == null){
            log.info("Proposer did not vote on contract: {} and streamer did not mark contract failed. StreamerVote.isViewerFlaggedComplete(): {}",
                    contractModel.getId(),
                    streamerVote.isViewerFlaggedComplete());
            voteOutcome = ContractState.COMPLETED;
        } else {
            // TODO: Implement community voting logic.
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
    private boolean proposerMarkedContractCompleted(@Nullable final VoteModel proposerVote) {
        return proposerVote != null && proposerVote.isViewerFlaggedComplete();
    }

    /**
     * Streamer marking a contract failed is a special case where funds should be returned to the proposer
     * without considering additional votes. The streamer has every incentive to mark a contract as complete, so if
     * they mark it as failed we trust them without considering other votes.
     */
    private boolean streamerMarkedContractFailed(@Nullable final VoteModel streamerVote) {
        return streamerVote != null && !streamerVote.isViewerFlaggedComplete();
    }

    private void assertUserCanVoteOnContract(@NonNull final UserModel voter, @NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();

        final UUID proposerId = contractModel.getProposer().getId();
        final boolean isVoterContractProposer = voter.getId().equals(proposerId);
        final boolean proposerHasVoted = getVoteByContractIdAndVoterId(contractId, proposerId).isPresent();

        final UUID streamerId = contractModel.getStreamer().getId();
        final boolean isVoterContractStreamer = voter.getId().equals(streamerId);
        final boolean streamerHasVoted = getVoteByContractIdAndVoterId(contractId, streamerId).isPresent();

        final boolean isUserAllowedToVote = isVoterContractProposer || isVoterContractStreamer;
        if (!isUserAllowedToVote) {
            throw new IllegalStateException(String.format("Unauthorized vote detected. User: %s attempted to vote on contractId: %s without having permissions to do so",
                    voter.getId(),
                    contractModel.getId()));
        }

        final boolean isProposerAndHasVoted = isVoterContractProposer && proposerHasVoted;
        final boolean isStreamerAndHasVoted = isVoterContractStreamer && streamerHasVoted;
        final boolean userHasVoted = isProposerAndHasVoted || isStreamerAndHasVoted;
        if(userHasVoted) {
            throw new IllegalStateException(String.format("Multiple votes detected. User: %s attempted to vote on contractId: %s more than once",
                    voter.getId(),
                    contractModel.getId()));
        }
    }

    private void assertContractIsSettleable(@NonNull final ContractModel contractModel) {
        final UUID contractId = contractModel.getId();
        if (contractModel.isCommunityContract()) {
            throw new IllegalStateException(String.format("Cannot operate on a community contract because the logic has not yet been implemented. Contract Id: %s", contractId));
        }

        if (contractModel.getState() != ContractState.ACTIVE && contractModel.getState() != ContractState.OPEN) {
            throw new IllegalStateException(String.format("Cannot operate on a contract that is not active or open. Contract Id: %s", contractId));
        }
    }

    private boolean isContractPastSettleTimestamp(@NonNull final ContractModel contractModel) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        return now.after(contractModel.getSettlesAt());
    }
}
