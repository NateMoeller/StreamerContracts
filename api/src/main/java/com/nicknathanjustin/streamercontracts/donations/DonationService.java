package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.users.UserModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface DonationService {

    /**
     * Creates the donation in the database.
     *
     * @param contractModel The contract entity donated to.
     * @param donator The donator user entity.
     * @param donationAmount The amount of money donated.
     * @param contractTimestamp The creation timestamp of the contract.
     * @param paypalPaymentId The paypal payment id of the transaction.
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
     * Settles a donation by either releasing money to streamer or returning money to donor.
     *
     * @param donationModel the donation to settle.
     * @param releaseDonation flag indicating if the donation should be released and the streamed paid. Or if the
     *                        donation should be returned to the donator.
     * @return true if donation was setteled correctly. False otherwise.
     * @throws IllegalArgumentException Thrown when donation has already been settled.
     */
    boolean settleDonation(DonationModel donationModel, boolean releaseDonation) throws IllegalArgumentException;
}
