package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    @NonNull final ContractModelRepository contractModelRepository;

    @Override
    public ContractModel createContract(@NonNull final UserModel proposer, @NonNull final UserModel streamer, @Nullable final String game, @NonNull final String description) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        // Add 10 days to the current timestamp to create the expires timestamp
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationTimestamp);
        calendar.add(Calendar.DAY_OF_WEEK, 10);
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
}
