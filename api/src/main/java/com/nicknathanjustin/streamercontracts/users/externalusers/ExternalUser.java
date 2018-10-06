package com.nicknathanjustin.streamercontracts.users.externalusers;

import lombok.Data;

@Data
public abstract class ExternalUser {

    protected String login;

    protected String displayName;

    protected String type;

    protected String broadcasterType;

    protected String description;

    protected String profileImageUrl;

    protected String offlineImageUrl;

    protected int viewCount;
}
