package com.nicknathanjustin.streamercontracts.emails;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @NonNull final EmailModelRepository emailModelRepository;

    @Override
    public void createEmail(@NonNull final String name, @NonNull final String email) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());

        emailModelRepository.save(EmailModel.builder()
                .name(name)
                .email(email)
                .createdAt(creationTimestamp)
                .build());
    }
}
