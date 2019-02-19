package com.nicknathanjustin.streamercontracts.users.dtos;

import com.nicknathanjustin.streamercontracts.settings.UserSettingsModel;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@EqualsAndHashCode(callSuper=true)
@Value
public class PublicUser extends User {

    private final String payPalEmail;

    public PublicUser(@NonNull final TwitchUser twitchUser) {
        super(twitchUser);
        this.payPalEmail = null;
    }

    public PublicUser(@NonNull final TwitchUser twitchUser, @Nullable final UserSettingsModel userSettingsModel) {
        super(twitchUser);
        this.payPalEmail = userSettingsModel != null ? userSettingsModel.getPaypalEmail() : null;
    }
}
