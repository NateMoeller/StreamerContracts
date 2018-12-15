package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.ContractDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ContractModelRepository extends CrudRepository<ContractModel, UUID> {

    @Query("SELECT contractModel " +
           "FROM ContractModel contractModel " +
           "WHERE contractModel.settlesAt < :currentTimestamp " +
           "AND contractModel.state = 'ACCEPTED'")
    Set<ContractModel> findAllSettleableContracts(@Param("currentTimestamp") Timestamp now);

    @Query("SELECT new com.nicknathanjustin.streamercontracts.contracts.dtos.ContractDto(" +
                "contractModel, " +
                "(SELECT SUM(donationModel.donationAmount) FROM DonationModel donationModel WHERE donationModel.contract.id = contractModel.id)) " +
           "FROM ContractModel contractModel " +
           "WHERE :currentTimestamp < contractModel.settlesAt " +
           "AND contractModel.state = 'OPEN' " +
           "AND contractModel.streamer.twitchUsername = :streamer")
    Set<ContractDto> findAllContractsForUserAndState(
            @Param("currentTimestamp") Timestamp now,
            @Param("streamer") String streamer);
}
