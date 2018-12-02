package com.nicknathanjustin.streamercontracts.settings;

import com.nicknathanjustin.streamercontracts.settings.requests.UpdateUserSettingsRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;

public interface UserSettingsService {

    /**
     * Creates or updates a user's settings
     *
     * @param UserModel the userModel to update settings for
     * @param updateUserSettingsRequest request object holding userSettings to update
     * @return the updated UserSettingsModel
     */
    UserSettingsModel updateUserSettings(UserModel UserModel, UpdateUserSettingsRequest updateUserSettingsRequest);
}
