package com.nicknathanjustin.streamercontracts.users.externalusers;

import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TwitchUser extends ExternalUser {

    public static Optional<TwitchUser> createTwitchUser(@NonNull final Map<String, List<Map<String, Object>>> details) {
        TwitchUser twitchUser = null;
        final List<Map<String, Object>> data = details.get("data");
        if (!CollectionUtils.isEmpty(data)) {
            final Map<String, Object> properties = data.get(0);
            twitchUser = new TwitchUser(
                    (String) properties.get("login"),
                    (String) properties.get("display_name"),
                    (String) properties.get("type"),
                    (String) properties.get("broadcaster_type"),
                    (String) properties.get("description"),
                    (String) properties.get("profile_image_url"),
                    (String) properties.get("offline_image_url"),
                    (int) properties.get("view_count")
            );
        }

        return Optional.ofNullable(twitchUser);
    }


    public TwitchUser(
            @NonNull final String login,
            @NonNull final String displayName,
            @NonNull final String type,
            @NonNull final String broadcasterType,
            @NonNull final String description,
            @NonNull final String profileImageUrl,
            @NonNull final String offlineImageUrl,
            final int viewCount) {
        this.login = login;
        this.displayName = displayName;
        this.type = type;
        this.broadcasterType = broadcasterType;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
        this.offlineImageUrl = offlineImageUrl;
        this.viewCount = viewCount;
    }
}
