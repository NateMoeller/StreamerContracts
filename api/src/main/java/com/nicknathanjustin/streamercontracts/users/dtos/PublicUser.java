package com.nicknathanjustin.streamercontracts.users.dtos;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;

public class PublicUser extends User{

    public PublicUser(@NonNull TwitchUser twitchUser) {
        super(twitchUser);
    }
}
