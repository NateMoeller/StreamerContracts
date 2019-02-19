package com.nicknathanjustin.streamercontracts.users.dtos;

import com.google.common.hash.Hashing;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@EqualsAndHashCode(callSuper=true)
@Data
public class PrivateUser extends User {

    private final String alertChannelId;

    private final long openContracts;

    private final long acceptedContracts;

    private final long declinedContracts;

    private final long expiredContracts;

    private final long completedContracts;

    private final long failedContracts;

    private final long disputedContracts;

    private final BigDecimal moneyEarned;

    public PrivateUser(
            @NonNull final TwitchUser twitchUser,
            @NonNull final UserModel userModel,
            final long openContracts,
            final long acceptedContracts,
            final long declinedContracts,
            final long expiredContracts,
            final long completedContracts,
            final long failedContracts,
            final long disputedContracts,
            final BigDecimal moneyEarned) {
        super(twitchUser);
        this.alertChannelId = Hashing.sha256()
                .hashString(userModel.getId().toString(), StandardCharsets.UTF_8)
                .toString();
        this.openContracts = openContracts;
        this.acceptedContracts = acceptedContracts;
        this.declinedContracts = declinedContracts;
        this.expiredContracts = expiredContracts;
        this.completedContracts = completedContracts;
        this.failedContracts = failedContracts;
        this.disputedContracts = disputedContracts;
        this.moneyEarned = moneyEarned;
    }
}
