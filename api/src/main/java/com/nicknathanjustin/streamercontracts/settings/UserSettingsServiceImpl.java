package com.nicknathanjustin.streamercontracts.settings;

import com.nicknathanjustin.streamercontracts.settings.requests.UpdateUserSettingsRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    @NonNull final UserSettingsModelRepository userSettingsModelRepository;

    @Override
    public UserSettingsModel updateUserSettings(@NonNull final UserModel userModel,
                                                @NonNull final UpdateUserSettingsRequest updateUserSettingsRequest) {
        final Timestamp now = new Timestamp(System.currentTimeMillis());
        final String paypalEmail = updateUserSettingsRequest.getPaypalEmail();
        final Optional<UserSettingsModel> optionalUserSettingsModel = userSettingsModelRepository.findByUserId(userModel.getId());
        final UserSettingsModel userSettingsModel = optionalUserSettingsModel.orElse(
                UserSettingsModel.builder()
                .user(userModel)
                .createdAt(now)
                .build()
        );

        userSettingsModel.setPaypalEmail(paypalEmail);
        userSettingsModel.setUpdatedAt(now);

        return userSettingsModelRepository.save(userSettingsModel);
    }

    @Override
    public Optional<UserSettingsModel> getUserSettings(@NonNull final UserModel UserModel) {
        return userSettingsModelRepository.findByUserId(UserModel.getId());
    }
}
