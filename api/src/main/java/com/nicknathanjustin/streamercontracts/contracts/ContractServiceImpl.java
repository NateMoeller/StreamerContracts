package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private static final int PAY_PAL_TRANSACTION_TIMEOUT_IN_DAYS = 3;

    @NonNull final ContractModelRepository contractModelRepository;

    @Override
    public ContractModel createContract(@NonNull final UserModel proposer, @NonNull final UserModel streamer, @Nullable final String game, @NonNull final String description) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationTimestamp);
        calendar.add(Calendar.DAY_OF_WEEK, PAY_PAL_TRANSACTION_TIMEOUT_IN_DAYS);
        final Timestamp expiresTimestamp = new Timestamp(calendar.getTime().getTime());

        return contractModelRepository.save(ContractModel.builder()
                .proposer(proposer)
                .streamer(streamer)
                .game(game)
                .description(description)
                .proposedAt(creationTimestamp)
                .expiresAt(expiresTimestamp)
                .acceptedAt(null)
                .completedAt(null)
                .isAccepted(false)
                .isCompleted(false)
                .isCommunityContract(false)
                .build());
    }

    @Override
    public Set<ContractModel> getExpiredContracts() {
        return contractModelRepository.findAllExpiredContracts(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public Optional<ContractModel> getContract(@NonNull final UUID contractId) {
        return contractModelRepository.findById(contractId);
    }

    @Override
    public void settlePayments(@NonNull final ContractModel contractModel, final boolean shouldReleasePayments) {
        //TODO: loop through donations for the contract and either void or capture payPalPayments
        contractModel.setIsCompleted(shouldReleasePayments);
        contractModel.setCompletedAt(new Timestamp(System.currentTimeMillis()));
        contractModelRepository.save(contractModel);
    }
}
