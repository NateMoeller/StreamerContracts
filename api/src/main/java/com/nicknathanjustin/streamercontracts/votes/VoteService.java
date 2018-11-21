package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.users.UserModel;

public interface VoteService {

    /**
     * Creates a vote given the supplied parameters
     *
     * @param voter User that voted on this contract
     * @param contract Contract user has voted on
     * @param flaggedCompleted indicates if the user considered the contract completed or not
     */
    void recordVote(UserModel voter, ContractModel contract, boolean flaggedCompleted);

    /**
     * Checks if voting is complete for the given contract. Voting being complete is independent of the contract
     * completing.
     *
     * @param contract contract to check
     * @return true if voting is complete enough to distribute contract donations. False otherwise
     */
    boolean isVotingComplete(ContractModel contract);

    /**
     * Looks at all votes for a given contract and returns the outcome of those votes. See implementation for what
     * defines each vote outcome.
     *
     * @param contract contract to check
     * @return VoteOutcome indicating the the result of all available votes for the given contract
     */
    VoteOutcome getVoteOutcome(ContractModel contract);
}
