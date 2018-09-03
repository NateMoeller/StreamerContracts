package com.nicknathanjustin.streamercontracts.users;

import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.List;
import java.util.Map;

@Data
public class TwitchUser {

    private final String login;

    private final String displayName;

    private final String type;

    private final String broadcasterType;

    private final String description;

    private final String profileImageUrl;

    private final String offlineImageUrl;

    private final int viewCount;

    public static TwitchUser createTwitchUser(@NonNull final OAuth2Authentication auth) {
        final Authentication userAuth = auth.getUserAuthentication();
        final Map<String, List<Map<String, Object>>> details = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
        final List<Map<String, Object>> data = details.get("data");
        final Map<String, Object> properties = data.get(0);
        return new TwitchUser(
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

    public TwitchUser(
            @NonNull final String login,
            @NonNull final String displayName,
            @NonNull final String type,
            @NonNull final String broadcasterType,
            @NonNull final String description,
            @NonNull final String profileImageUrl,
            @NonNull final String offlineImageUrl,
            @NonNull final int viewCount) {
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
