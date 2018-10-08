package com.nicknathanjustin.streamercontracts.users.dtos;

import com.google.common.hash.Hashing;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.Data;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;

@Data
public class PrivateUser extends User{

    private final String alertChannelId;

    public PrivateUser(@NonNull TwitchUser twitchUser, @NonNull UserModel userModel) {
        super(twitchUser);
        this.alertChannelId = Hashing.sha256()
                .hashString(userModel.getId().toString(), StandardCharsets.UTF_8)
                .toString();
    }
}
