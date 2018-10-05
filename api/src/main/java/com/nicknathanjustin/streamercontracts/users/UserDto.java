package com.nicknathanjustin.streamercontracts.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.hash.Hashing;
import lombok.Data;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;

@Data
public class UserDto {
    @JsonView(View.PublicUser.class)
    private final String displayName;

    @JsonView(View.PublicUser.class)
    private final String type;

    @JsonView(View.PublicUser.class)
    private final String broadcasterType;

    @JsonView(View.PublicUser.class)
    private final String description;

    @JsonView(View.PublicUser.class)
    private final String profileImageUrl;

    private final int viewCount;

    private final String alertChannelId;

    public UserDto(@NonNull TwitchUser twitchUser, @NonNull UserModel userModel) {
        this.displayName = twitchUser.getDisplayName();
        this.type = twitchUser.getType();
        this.broadcasterType = twitchUser.getBroadcasterType();
        this.description = twitchUser.getDescription();
        this.profileImageUrl = twitchUser.getProfileImageUrl();
        this.viewCount = twitchUser.getViewCount();
        this.alertChannelId = Hashing.sha256()
                .hashString(userModel.getId().toString(), StandardCharsets.UTF_8)
                .toString();
    }
}
