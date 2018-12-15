package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.ContractDto;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import org.apache.http.annotation.Contract;

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
     * Gets all setteable contracts.
     *
     * @return a set of all settleable contracts
     */
    Set<ContractModel> getSettleableContracts();

    /**
     * Gets a contract for the given ID
     *
     * @param contractId to retrieve contract by
     * @return the contract associated with the provided ID
     */
    Optional<ContractModel> getContract(UUID contractId);


    /**
     * Changes the state of a contract. All contract state transitions are expected to use this function.
     * Do not explicitly set the state.
     *
     * @param contractModel contract to settle payment for
     * @param newContractState The new contract state after the result of a vote.
     */
    void setContractState(ContractModel contractModel, ContractState newContractState);

    /**
     * Gets all contracts in the given state for the given user.
     *
     * @param user The user to retrieve contracts by.
     * @param state The state to retrieve contracts by.
     * @return a set of all contracts in the given state for the given user.
     */
    Set<ContractDto> getContractsForUserAndState(UserModel user, ContractState state);
}
