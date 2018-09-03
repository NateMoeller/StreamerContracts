package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;
import java.sql.Timestamp;

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
    public UserModel getUser(@NonNull final String twitchUsername) {
        List<UserModel> users = userModelRepository.findByTwitchUsername(twitchUsername);
        Assert.isTrue(users.size() <= 1, "Error. There are multiple users in the database with the same twitch username.");
        return users.get(0);
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
