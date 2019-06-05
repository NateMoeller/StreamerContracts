package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.alerts.AlertService;
import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static com.nicknathanjustin.streamercontracts.donations.DonationState.ERROR;
import static com.nicknathanjustin.streamercontracts.donations.DonationState.PAID_TO_STREAMER;
import static com.nicknathanjustin.streamercontracts.donations.DonationState.RETURNED_TO_DONOR;

@RequiredArgsConstructor
public class DonationServiceImpl implements  DonationService{

    @NonNull private final AlertService alertService;
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
                .donor(donator)
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

        DonationState donationState;
        final boolean freeDonation = donationModel.getDonationAmount().compareTo(BigDecimal.ZERO) == 0;
        if (freeDonation){
            donationState = releaseDonation ? PAID_TO_STREAMER : RETURNED_TO_DONOR;
        } else if (releaseDonation) {
            donationState = paymentsService.capturePayment(donationModel.getPaypalAuthorizationId()) ?
                    PAID_TO_STREAMER :
                    ERROR;
        } else {
            donationState = paymentsService.voidPayment(donationModel.getPaypalAuthorizationId()) ?
                    RETURNED_TO_DONOR :
                    ERROR;
        }

        donationModel.setDonationState(donationState);
        if (donationState != ERROR) {
            donationModel.setReleasedAt(new Timestamp(System.currentTimeMillis()));
            notifyStreamerAndDonor(donationModel, donationState);
        }

        donationModelRepository.save(donationModel);
        return donationState != ERROR;
    }

    private void notifyStreamerAndDonor(@NonNull final DonationModel donationModel, @NonNull final DonationState donationState) {
        final UserModel donor = donationModel.getDonor();
        final UserModel streamer = donationModel.getContract().getStreamer();
        final String description = donationModel.getContract().getDescription();

        String donorMessage;
        String streamerMessage;
        if (donationModel.getContract().getState().equals(ContractState.DECLINED)) {
            donorMessage = streamer.getTwitchUsername() + " declined your bounty.";
            streamerMessage = " you declined " + donor.getTwitchUsername() + "'s bounty.";
        } else {
            donorMessage = donationState == PAID_TO_STREAMER ?
                    streamer.getTwitchUsername() + " completed your bounty!" :
                    streamer.getTwitchUsername() + " failed your bounty.";
            streamerMessage = donationState == PAID_TO_STREAMER ?
                    "You completed " + donor.getTwitchUsername() + " bounty!" :
                    "You failed " + donor.getTwitchUsername() + " bounty.";
        }

        alertService.sendNotification(donor, donorMessage, description);
        alertService.sendNotification(streamer, streamerMessage, description);
    }
}
