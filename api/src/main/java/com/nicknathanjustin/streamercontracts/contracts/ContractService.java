package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.users.UserModel;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ContractService {

    /**
     * Creates the contract in the database.
     *
     * @param  proposer The UserModel that proposed the challenge.
     * @param  streamer The UserModel of the streamer.
     * @param  game The name of the game being streamed.
     * @param  description The description of the contract.
     */
    ContractModel createContract(UserModel proposer, UserModel streamer, String game, String description);

    /**
     * Gets all expired contracts.
     *
     * @return a set of all expired contracts
     */
    Set<ContractModel> getExpiredContracts();

    /**
     * Gets a contract for the given ID
     *
     * @param contractId to retrieve contract by
     * @return the contract associated with the provided ID
     */
    Optional<ContractModel> getContract(UUID contractId);


    /**
     * Loop through all donations for a contract and either void or capture those payments
     *
     * @param contractModel contract to settle payment for
     * @param shouldReleasePayments flag indicating if payments should be released to the streamer. If false the payment
     *                              Is voided and never leaves the donator's account.
     */
    void settlePayments(ContractModel contractModel, boolean shouldReleasePayments);
}
