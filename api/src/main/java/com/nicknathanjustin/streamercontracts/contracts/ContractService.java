package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
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
     * @param contractModel contract to set state on
     * @param newContractState New contract state.
     */
    void setContractState(ContractModel contractModel, ContractState newContractState);

    /**
     * Settles payments for a contract by releasing each donation to either the donor or the streamer.
     *
     * @param contractModel the contract to settle payment for.
     */
    void settlePayments(ContractModel contractModel);

    /**
     * Gets all contracts in the given state for the given user.
     *
     * @param streamer The user to retrieve contracts by.
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @param requestedByStreamer Boolean that indicates if the request was by the streamer.
     * @return a set of all contracts in the given state for the given user.
     */
    Page<Contract> getContractsForStreamerAndState(UserModel streamer, ContractState state, Pageable pageable, boolean requestedByStreamer);

    /**
     * Gets all contracts for the given user.
     *
     * @param streamer The user to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @param requestedByStreamer Boolean that indicates if the request was by the streamer.
     * @return a set of all contracts for the given user.
     */
    Page<Contract> getContractsForStreamer(UserModel streamer, Pageable pageable, boolean requestedByStreamer);

    /**
     * Gets all contracts in the given state.
     *
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts in the given state for the given user.
     */
    Page<Contract> getContractsForState(ContractState state, Pageable pageable);

    /**
     * Gets all contracts.
     *
     * @param pageable identifies the page number and pagesize to retrieve
     * @return all of the contracts.
     */
    Page<Contract> getAllContracts(Pageable pageable);

    /**
     * Gets all contracts where the contract contains a donation from the given proposer
     * and is in the given state.
     *
     * @param donor The user to retrieve contracts by.
     * @param state The state to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return all contracts where the contract contains a donation from the given proposer
     * and is in the given state.
     */
    Page<Contract> getContractsForDonorAndState(UserModel donor, ContractState state, Pageable pageable);

    /**
     * Gets all contracts where the contract contains a donation from the given proposer.
     *
     * @param donor The user to retrieve contracts by.
     * @param pageable identifies the page number and pagesize to retrieve
     * @return a set of all contracts in the given state for the given user.
     */
    Page<Contract> getContractsForDonor(UserModel donor, Pageable pageable);

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
     * @param state The state to retrieve the total money by.
     * @return the total amount of money the streamer has earned.
     */
    BigDecimal getMoneyForStreamerAndState(UserModel streamer, ContractState state);
    
    /**
     * Activates the contract.
     *
     * @param contractModel The contract to activate.
     */
    void activateContract(ContractModel contractModel);

    /**
     * Deactivates the contract.
     *
     * @param contractModel The contract to deactivate.
     */
    void deactivateContract(ContractModel contractModel);
}
