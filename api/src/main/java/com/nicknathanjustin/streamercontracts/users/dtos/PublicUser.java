package com.nicknathanjustin.streamercontracts.users.dtos;

import com.nicknathanjustin.streamercontracts.settings.UserSettingsModel;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class PublicUser extends User{

    private final boolean hasPayPalEmail;

    public PublicUser(@NonNull TwitchUser twitchUser, @Nullable UserSettingsModel userSettingsModel) {
        super(twitchUser);
        this.hasPayPalEmail = userSettingsModel != null && userSettingsModel.getPaypalEmail() != null;
    }
}
