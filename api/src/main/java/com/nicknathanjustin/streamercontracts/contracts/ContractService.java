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
     * Changes the state of a contract.
     *
     * @param contractModel contract to settle payment for
     * @param newContractState The new contract state after the result of a vote.
     */
    void setContractState(ContractModel contractModel, ContractState newContractState);
}
