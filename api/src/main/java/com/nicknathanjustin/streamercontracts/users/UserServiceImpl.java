package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.sql.Timestamp;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull final UserModelRepository userModelRepository;

    @Override
    public UserModel createUser(@NonNull String twitchUsername) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        return userModelRepository.save(UserModel.builder()
                .twitchUsername(twitchUsername)
                .totalLogins(1)
                .createdAt(creationTimestamp)
                .lastLogin(creationTimestamp)
                .build());
    }

    @Override
    public boolean userExists(@NonNull String twitchUsername) {
        final Long count = userModelRepository.countByTwitchUsername(twitchUsername);
        Assert.isTrue(count <= 1, "Error. There are multiple users in the database with the same twitch username.");
        return count == 1;
    }

    @Override
    public void login(@NonNull String twichUsername) {
        final Timestamp loginTimestamp = new Timestamp(System.currentTimeMillis());
        UserModel user = userModelRepository.findByTwitchUsername(twichUsername);
        int totalLogins = user.getTotalLogins() + 1;
        user.setTotalLogins(totalLogins);
        user.setLastLogin(loginTimestamp);
        userModelRepository.save(user);
    }
}
