package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull final UserModelRepository userModelRepository;

    @Override
    public UserModel createUser(@NonNull String twitchUsername) {
        Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        return userModelRepository.save(UserModel.builder()
                .twitchUsername(twitchUsername)
                .totalLogins(1)
                .createTimestamp(creationTimestamp)
                .lastLogin(creationTimestamp)
                .build());
    }
}
