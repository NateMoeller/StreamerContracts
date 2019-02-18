package com.nicknathanjustin.streamercontracts.settings.requests;

import org.springframework.lang.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UpdateUserSettingsRequest {
    @NonNull private String paypalEmail;
    @Nullable private Boolean isBusinessEmail;
}
