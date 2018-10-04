package com.nicknathanjustin.streamercontracts.contracts;

import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Calendar;
import java.util.UUID;
import java.sql.Timestamp;

@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    @NonNull final ContractModelRepository contractModelRepository;

    @Override
    public ContractModel createContract(@NonNull final UUID proposerId, @NonNull final UUID streamerId, @Nullable final String game, @NonNull final String description) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        // Add 10 days to the current timestamp to create the expires timestamp
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationTimestamp);
        calendar.add(Calendar.DAY_OF_WEEK, 10);
        final Timestamp expiresTimestamp = new Timestamp(calendar.getTime().getTime());
        return contractModelRepository.save(ContractModel.builder()
                .proposerId(proposerId)
                .streamerId(streamerId)
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
}
