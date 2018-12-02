package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.users.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface VoteService {

    /**
     * Creates a vote given the supplied parameters
     *
     * @param voter User that voted on this contract
     * @param contract Contract user has voted on
     * @param flaggedCompleted indicates if the user considered the contract completed or not
     * @return Returns true if the vote was successfully recorded.
     */
    boolean recordVote(UserModel voter, ContractModel contract, boolean flaggedCompleted);

    /**
     * Checks if voting is complete for the given contract. Voting being complete is independent of the contract
     * completing.
     *
     * @param optionalProposerVote The proposers vote.
     * @param optionalStreamerVote The streamers vote.
     * @param contract contract to check
     * @return true if voting is complete enough to distribute contract donations. False otherwise
     */
    boolean isVotingComplete(Optional<VoteModel> optionalProposerVote,
                             Optional<VoteModel> optionalStreamerVote,
                             ContractModel contract);

    /**
     * Looks at all votes for a given contract and returns the outcome of those votes. See implementation for what
     * defines each vote outcome.
     *
     * @param contract contract to check
     * @return Returns the state the contract should transition to.
     */
    ContractState getVoteOutcome(ContractModel contract);

    /**
     * Gets a vote for a given contract id and user id.
     *
     * @param contractId The contract id associated with the vote.
     * @param userId The user id associated with the vote.
     * @return Returns an optional Vote.
     */
    Optional<VoteModel> getVoteByContractIdAndVoterId(UUID contractId, UUID userId);
}
