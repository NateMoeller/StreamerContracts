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
        final Optional<UserSettingsModel> optionalUserSettingsModel = userSettingsModelRepository.findByUserId(userModel.getId());
        final Timestamp createdAt = optionalUserSettingsModel.isPresent() ?
                optionalUserSettingsModel.get().getCreatedAt() :
                new Timestamp(System.currentTimeMillis());
        final UserSettingsModel userSettingsModel = UserSettingsModel.builder()
                .user(userModel)
                .paypalEmail(updateUserSettingsRequest.getPaypalEmail())
                .createdAt(createdAt)
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        return userSettingsModelRepository.save(userSettingsModel);
    }
}
