package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService{

    @NonNull final VoteModelRepository voteModelRepository;

    @Override
    public void recordVote(@NonNull final UserModel voter,
                           @NonNull final ContractModel contractModel,
                           @NonNull final boolean flaggedCompleted) {
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
        final VoteModel proposerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getProposer().getId());
        final VoteModel streamerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getStreamer().getId());
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        if (proposerVote != null && streamerVote != null) {
            return true;
        } else if (proposerMarkedContractCompleted(contractModel)) {
            return true;
        } else if (streamerMarkedContractFailed(contractModel)) {
            return false;
        } else if (now.after(contractModel.getExpiresAt())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean wasContractCompleted(@NonNull final ContractModel contractModel) {
        if (!isVotingComplete(contractModel)) {
            throw new IllegalArgumentException("Attempted to check if contract was completed before voting was finished. ContractId: " + contractModel.getId());
        }

        final UUID contractId = contractModel.getId();
        final VoteModel proposerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getProposer().getId());
        final VoteModel streamerVote = voteModelRepository.findByContractIdAndVoterId(contractId, contractModel.getStreamer().getId());
        if (proposerMarkedContractCompleted(contractModel)) {
            //Proposer has marked contract as completed. Release funds without reading Streamer's vote.
            return true;
        } else if (streamerMarkedContractFailed(contractModel)) {
            //Streamer has marked contract as failed. Return funds without reading Proposers's vote.
            return false;
        } else if(proposerVote == null && streamerVote == null) {
            //Neither proposer or streamer voted. Release funds if contract was accepted. Return funds otherwise.
            return contractModel.isAccepted();
        } else if(proposerVote != null) {
            //Streamer did not vote. Take Proposer's vote as the final outcome
            return proposerVote.isViewerFlaggedComplete();
        } else if(streamerVote != null){
            //Proposer did not vote. Take Streamer's vote as the final outcome
            return streamerVote.isViewerFlaggedComplete();
        }
        //Default case is to consider the contract completed and release money to the streamer.
        return true;
    }

    private boolean proposerMarkedContractCompleted(@NonNull final ContractModel contractModel) {
        final VoteModel proposerVote = voteModelRepository.findByContractIdAndVoterId(contractModel.getId(), contractModel.getProposer().getId());
        return proposerVote != null && proposerVote.isViewerFlaggedComplete();
    }

    private boolean streamerMarkedContractFailed(@NonNull final ContractModel contractModel) {
        final VoteModel streamerVote = voteModelRepository.findByContractIdAndVoterId(contractModel.getId(), contractModel.getStreamer().getId());
        return streamerVote != null && !streamerVote.isViewerFlaggedComplete();
    }

    private boolean userCanVoteOnContract(@NonNull final UserModel voter, @NonNull final ContractModel contractModel) {
        validateContract(contractModel);

        final UUID contractId = contractModel.getId();

        final UUID proposerId = contractModel.getProposer().getId();
        final boolean isVoterContractProposer = voter.getId().equals(proposerId);
        final boolean proposerHasVoted = (voteModelRepository.findByContractIdAndVoterId(contractId, proposerId) != null);

        final UUID streamerId = contractModel.getStreamer().getId();
        final boolean isVoterContractStreamer = voter.getId().equals(streamerId);
        final boolean streamerHasVoted = (voteModelRepository.findByContractIdAndVoterId(contractId, streamerId) == null);

        final boolean isUserAllowedToVote = isVoterContractProposer || isVoterContractStreamer;
        if (!isUserAllowedToVote) {
            log.warn("Unauthorized vote detected. User: {} attempted to vote on contractId: {} without having permissions to do so",
                    voter.getId(),
                    contractModel.getId());
        }

        final boolean isProposerAndHasVoted = isVoterContractProposer && proposerHasVoted;
        final boolean isStreamerAndHasVoted = isVoterContractStreamer && streamerHasVoted;
        final boolean userHasVoted = isProposerAndHasVoted || isStreamerAndHasVoted;
        if(userHasVoted) {
            log.warn("Multiple votes detected. User: {} attempted to vote on contractId: {} more than once",
                    voter.getId(),
                    contractModel.getId());
        }
        return isUserAllowedToVote && !userHasVoted;
    }

    private void validateContract(@NonNull final ContractModel contractModel) {
        if (contractModel.isCommunityContract()) {
            throw new IllegalArgumentException("Voting logic not yet implemented for community contracts. ContractId " + contractModel.getId());
        }

        if (!contractModel.isAccepted()) {
            throw new IllegalArgumentException("Cannot vote on an unaccepted contract. ContractId " + contractModel.getId());
        }

        if (contractModel.getIsCompleted() != null) {
            throw new IllegalArgumentException("Cannot vote on a completed contract. ContractId " + contractModel.getId());
        }
    }
}
