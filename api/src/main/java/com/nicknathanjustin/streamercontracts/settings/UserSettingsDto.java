package com.nicknathanjustin.streamercontracts.settings;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSettingsDto {
    private final String paypalEmail;

    public UserSettingsDto(@NonNull UserSettingsModel userSettingsModel) {
        this.paypalEmail = userSettingsModel.getPaypalEmail();
    }
}
