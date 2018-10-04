package com.nicknathanjustin.streamercontracts.donations;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@RequiredArgsConstructor
public class DonationServiceImpl implements  DonationService{

    @NonNull final DonationModelRepository donationModelRepository;

    @Override
    public void createDonation(
            @NonNull final UUID contractId,
            @NonNull final UUID donatorId,
            @NonNull final BigDecimal donationAmount,
            @NonNull final Timestamp contractTimestamp,
            @NonNull final String paypalPaymentId) {
        donationModelRepository.save(DonationModel.builder()
                .contractId(contractId)
                .donatorId(donatorId)
                .donatedAt(contractTimestamp)
                .donationAmount(donationAmount)
                .paypalPaymentId(paypalPaymentId)
                .build());
    }
}
