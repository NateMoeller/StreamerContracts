package com.nicknathanjustin.streamercontracts.donations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.UUID;

public interface DonationModelRepository extends CrudRepository<DonationModel, UUID> {

    @Query("SELECT new com.nicknathanjustin.streamercontracts.donations.OpenDonationDto(" +
            "donationModel.id, " +
            "donationModel.donationAmount, " +
            "donationModel.contract.description, " +
            "donationModel.contract.streamer.twitchUsername) " +
           "FROM DonationModel donationModel " +
           "WHERE donationModel.donator.id = :donatorId " +
           "AND donationModel.contract.state = 'ACCEPTED' " +
           "AND donationModel.contract.settlesAt > :currentTimestamp")
    Page<OpenDonationDto> findAllDonationsForAcceptedContracts(@Param("donatorId") UUID donatorId,
                                                               @Param("currentTimestamp") Timestamp now,
                                                               Pageable pageable);
}
