package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ContractModelRepository extends CrudRepository<ContractModel, UUID> {
	
    List<ContractModel> findAllByStateAndStreamerOrderByActivatedAtDesc(ContractState state, UserModel streamer);

    long countByStateAndStreamer(ContractState state, UserModel streamer);
 
    @Query("SELECT contractModel " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.settlesAt < :currentTimestamp " +
           "AND contractModel.state = 'ACTIVE' OR contractModel.state = 'OPEN' " +
           "ORDER BY contractModel.proposedAt DESC")
    Set<ContractModel> findAllSettleableContracts(@Param("currentTimestamp") Timestamp now);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PrivateContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.state = :state " +
           "AND contractModel.streamer.twitchUsername = :streamer " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPrivateContractsForStreamerAndState(@Param("streamer") String streamer, @Param("state") ContractState state, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PrivateContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.streamer.twitchUsername = :streamer " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPrivateContractsForStreamer(@Param("streamer") String streamer, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PublicContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.state = :state " +
           "AND contractModel.streamer.twitchUsername = :streamer " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPublicContractsForStreamerAndState(@Param("streamer") String streamer, @Param("state") ContractState state, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PublicContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.streamer.twitchUsername = :streamer " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPublicContractsForStreamer(@Param("streamer") String streamer, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PublicContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.state = :state " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPublicContractsForState(@Param("state") ContractState state, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PublicContract(" +
                "contractModel) " +
           "FROM ContractModel contractModel " +
           "ORDER BY contractModel.proposedAt DESC")
    Page<Contract> findAllPublicContracts(Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PrivateContract(" +
                "donationModel.contract) " +
           "FROM DonationModel donationModel " +
           "WHERE donationModel.donor.twitchUsername = :donor " +
           "AND donationModel.contract.state = :state " +
           "ORDER BY donationModel.contract.proposedAt DESC")
    Page<Contract> findAllContractsForDonorAndState(@Param("donor") String donor, @Param("state") ContractState state, Pageable pageable);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.PrivateContract(" +
                "donationModel.contract) " +
           "FROM DonationModel donationModel " +
           "WHERE donationModel.donor.twitchUsername = :donor " +
           "ORDER BY donationModel.contract.proposedAt DESC")
    Page<Contract> findAllContractsForDonor(@Param("donor") String donor, Pageable pageable);

    @Query("SELECT SUM(d.donationAmount) " +
           "FROM DonationModel d " +
           "WHERE d.contract.state = :state " +
           "AND d.contract.streamer.twitchUsername = :streamer")
    BigDecimal getMoneyForStreamerAndState(@Param("streamer") String streamer, @Param("state") ContractState state);
}
