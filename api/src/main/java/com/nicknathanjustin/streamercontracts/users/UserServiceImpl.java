package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull final UserModelRepository userModelRepository;

    @Override
    public UserModel createUser(@NonNull final String twitchUsername) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        return userModelRepository.save(UserModel.builder()
                .twitchUsername(twitchUsername)
                .totalLogins(1)
                .createdAt(creationTimestamp)
                .lastLogin(creationTimestamp)
                .build());
    }

    @Override
    public Optional<UserModel> getUser(@NonNull final String twitchUsername) {
        return Optional.ofNullable(userModelRepository.findByTwitchUsername(twitchUsername));
    }

    @Override
    public void login(@NonNull final UserModel user) {
        final Timestamp loginTimestamp = new Timestamp(System.currentTimeMillis());
        final int totalLogins = user.getTotalLogins() + 1;
        user.setTotalLogins(totalLogins);
        user.setLastLogin(loginTimestamp);
        userModelRepository.save(user);
    }
}
