package com.nicknathanjustin.streamercontracts.donations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public interface DonationService {

    /**
     * Creates the donation in the database.
     *
     * @param  contractId The UUID of the contract that corresponds to this donation.
     * @param  donatorId The UUID of the user that donated.
     * @param  donationAmount The amount of money donated.
     * @param  contractTimestamp The creation timestamp of the contract.
     * @param  paypalPaymentId The paypal payment id of the transaction.
     */
    void createDonation(UUID contractId, UUID donatorId, BigDecimal donationAmount, Timestamp contractTimestamp, String paypalPaymentId);
}
