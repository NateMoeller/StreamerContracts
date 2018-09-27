package com.nicknathanjustin.streamercontracts.contracts;

import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.util.Calendar;
import java.util.UUID;
import java.sql.Timestamp;

@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    @NonNull final ContractModelRepository contractModelRepository;

    @Override
    public void createContract(@NonNull UUID proposerId, @NonNull UUID streamerId, @NonNull String game, @NonNull String description) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        // Add 10 days to the current timestamp to create the expires timestamp
        final Calendar cal = Calendar.getInstance();
        cal.setTime(creationTimestamp);
        cal.add(Calendar.DAY_OF_WEEK, 10);
        final Timestamp expiresTimestamp = new Timestamp(cal.getTime().getTime());
        contractModelRepository.save(ContractModel.builder()
                .proposerId(proposerId)
                .streamerId(streamerId)
                .game(game)
                .description(description)
                .proposedAt(creationTimestamp)
                .expiresAt(expiresTimestamp)
                .isAccepted(false)
                .isCompleted(false)
                .isCommunityContract(false)
                .build());
    }
}
