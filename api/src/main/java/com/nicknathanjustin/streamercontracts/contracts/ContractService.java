package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.ContractDto;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
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
     * @param streamer The user to retrieve contracts by.
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts in the given state for the given user.
     */
    Page<ContractDto> getContractsForStreamerAndState(UserModel streamer, ContractState state, Pageable pageable);

    /**
     * Gets all contracts for the given user.
     *
     * @param streamer The user to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts for the given user.
     */
    Page<ContractDto> getContractsForStreamer(UserModel streamer, Pageable pageable);

    /**
     * Gets all contracts in the given state.
     *
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts in the given state for the given user.
     */
    Page<ContractDto> getContractsForState(ContractState state, Pageable pageable);

    /**
     * Gets all contracts.
     *
     * @param pageable identifies the page number and pagesize to retrieve
     * @return all of the contracts.
     */
    Page<ContractDto> getAllContracts(Pageable pageable);

    /**
     * Gets all contracts where the contract contains a donation from the given proposer
     * and is in the given state.
     *
     * @param donator The user to retrieve contracts by.
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return all contracts where the contract contains a donation from the given proposer
     * and is in the given state.
     */
    Page<ContractDto> getContractsForDonatorAndState(UserModel donator, ContractState state, Pageable pageable);

    /**
     * Gets all contracts where the contract contains a donation from the given proposer.
     *
     * @param donator The user to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts in the given state for the given user.
     */
    Page<ContractDto> getContractsForDonator(UserModel donator, Pageable pageable);

    /**
     * Counts the contracts given the state and the streamer.
     *
     * @param state The state to retrieve contracts by.
     * @param streamer The user to retrieve contracts by.
     * @return number of contracts in the given state.
     */
    long countByStateAndStreamer(ContractState state, UserModel streamer);

    /**
     * Gets the total amount of money earned for the streamer.
     *
     * @param streamer The user object.
     * @return the total amount of money the streamer has earned.
     */
    BigDecimal getMoneyEarnedForStreamer(UserModel streamer);
}
