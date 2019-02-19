package com.nicknathanjustin.streamercontracts.users.dtos;

import com.nicknathanjustin.streamercontracts.users.externalusers.ExternalUser;
import lombok.Data;
import lombok.NonNull;

@Data
public abstract class User {

    private final String externalId;
    
    private final String displayName;

    private final String type;

    private final String broadcasterType;

    private final String description;

    private final String profileImageUrl;

    private final int viewCount;

    public User(@NonNull final ExternalUser externalUser) {
        this.externalId = externalUser.getExternalId();
        this.displayName = externalUser.getDisplayName();
        this.type = externalUser.getType();
        this.broadcasterType = externalUser.getBroadcasterType();
        this.description = externalUser.getDescription();
        this.profileImageUrl = externalUser.getProfileImageUrl();
        this.viewCount = externalUser.getViewCount();
    }
}
