package com.nicknathanjustin.streamercontracts.settings;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserSettingsDto {

    private final String paypalEmail;
    
    private final Boolean isBusinessEmail;

    public UserSettingsDto(@NonNull UserSettingsModel userSettingsModel) {
        this.paypalEmail = userSettingsModel.getPaypalEmail();
        this.isBusinessEmail = userSettingsModel.getIsBusinessEmail();
    }
}
