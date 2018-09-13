package com.nicknathanjustin.streamercontracts.users;

import com.google.common.hash.Hashing;
import lombok.Data;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;

@Data
public class User {
    private final String displayName;

    private final String type;

    private final String broadcasterType;

    private final String description;

    private final String profileImageUrl;

    private final int viewCount;

    private final String alertKey;

    public User (@NonNull TwitchUser twitchUser, @NonNull UserModel userModel) {
        this.displayName = twitchUser.getDisplayName();
        this.type = twitchUser.getType();
        this.broadcasterType = twitchUser.getBroadcasterType();
        this.description = twitchUser.getDescription();
        this.profileImageUrl = twitchUser.getProfileImageUrl();
        this.viewCount = twitchUser.getViewCount();
        this.alertKey = Hashing.sha256()
                .hashString(userModel.getId().toString(), StandardCharsets.UTF_8)
                .toString();
    }
}
