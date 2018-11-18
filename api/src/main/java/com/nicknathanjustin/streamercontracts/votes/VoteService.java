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
     * Checks if voting is complete for the given contract
     *
     * @param contract contract to check
     * @return true is voting is complete enough to distribute contract donations. False otherwise
     */
    boolean isVotingComplete(ContractModel contract);

    /**
     * Checks if a contract was completed and payments should be released to streamer. See implementation for what is
     * considered a completed contract.
     *
     * @param contract contract to check
     * @return true if contract donations should be released to a streamer. False otherwise.
     */
    boolean wasContractCompleted(ContractModel contract);
}
