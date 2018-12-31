package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DonationServiceImpl implements  DonationService{

    @NonNull final DonationModelRepository donationModelRepository;
    @NonNull final PaymentsService paymentsService;

    @Override
    public void createDonation(
            @NonNull final ContractModel contractModel,
            @NonNull final UserModel donator,
            @NonNull final BigDecimal donationAmount,
            @NonNull final Timestamp contractTimestamp,
            @NonNull final String paypalPaymentId,
            @NonNull final String paypalAuthorizationId) {
        donationModelRepository.save(DonationModel.builder()
                .contract(contractModel)
                .donator(donator)
                .createdAt(contractTimestamp)
                .donationAmount(donationAmount)
                .paypalPaymentId(paypalPaymentId)
                .paypalAuthorizationId(paypalAuthorizationId)
                .build());
    }

    @Override
    public Optional<DonationModel> getDonation(@NonNull final UUID donationId) {
        return donationModelRepository.findById(donationId);
    }

    @Override
    public boolean settleDonation(@NonNull final DonationModel donationModel, final boolean releaseDonation) {
        if (donationModel.getReleasedAt() != null) {
            throw new IllegalArgumentException("Attempted to settle already released donation: " + donationModel.getId());
        }

        boolean donationSettled;
        if (releaseDonation) {
            donationSettled = paymentsService.capturePayment(donationModel.getPaypalAuthorizationId());
        } else {
            donationSettled = paymentsService.voidPayment(donationModel.getPaypalAuthorizationId());
        }

        final DonationState donationState = getDonationState(donationSettled, releaseDonation);
        donationModel.setDonationState(donationState);
        if (donationState != DonationState.ERROR) {
            donationModel.setReleasedAt(new Timestamp(System.currentTimeMillis()));
        }

        donationModelRepository.save(donationModel);
        return donationSettled;
    }

    private DonationState getDonationState(final boolean donationSettled, final boolean releaseDonation) {
        if (!donationSettled) {
            return DonationState.ERROR;
        } else if (releaseDonation) {
            return DonationState.PAID_TO_STREAMER;
        } else {
            return DonationState.RETURNED_TO_DONOR;
        }
    }
}
