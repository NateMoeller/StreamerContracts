package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.donations.dtos.DonationDto;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DonationServiceImpl implements  DonationService{

    @NonNull final DonationModelRepository donationModelRepository;

    @Override
    public void createDonation(
            @NonNull final ContractModel contractModel,
            @NonNull final UserModel donator,
            @NonNull final BigDecimal donationAmount,
            @NonNull final Timestamp contractTimestamp,
            @NonNull final String paypalPaymentId) {
        donationModelRepository.save(DonationModel.builder()
                .contract(contractModel)
                .donator(donator)
                .donatedAt(contractTimestamp)
                .donationAmount(donationAmount)
                .paypalPaymentId(paypalPaymentId)
                .build());
    }

    @Override
    public Optional<DonationModel> getDonation(@NonNull final UUID donationId) {
        return donationModelRepository.findById(donationId);
    }
}
