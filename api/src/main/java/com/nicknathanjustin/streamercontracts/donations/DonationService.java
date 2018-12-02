package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface DonationService {

    /**
     * Creates the donation in the database.
     *
     * @param  contractModel The contract entity donated to.
     * @param  donator The donator user entity.
     * @param  donationAmount The amount of money donated.
     * @param  contractTimestamp The creation timestamp of the contract.
     * @param  paypalPaymentId The paypal payment id of the transaction.
     */
    void createDonation(ContractModel contractModel, UserModel donator, BigDecimal donationAmount, Timestamp contractTimestamp, String paypalPaymentId);

    /**
     * Gets a donation from the database given the donationId.
     *
     * @param donationId the id for the donation.
     * @return An Optional<DonationModel> for the user.
     */
    Optional<DonationModel> getDonation(UUID donationId);

    /**
     * Gets a paged DonationDto result.
     *
     * @param donatorId Filter open donations to only include those created by the supplied donatorId
     * @param pageable identifies the page number and pagesize to retrieve
     * @return A paged DonationDto result
     */
    Page<OpenDonationDto> listDonationsForAcceptedContracts(UUID donatorId, Pageable pageable);
}
