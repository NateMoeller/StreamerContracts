package com.nicknathanjustin.streamercontracts.settings;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserSettingsDto {
    private final String paypalEmail;

    public UserSettingsDto(@NonNull UserSettingsModel userSettingsModel) {
        this.paypalEmail = userSettingsModel.getPaypalEmail();
    }
}
