package com.nicknathanjustin.streamercontracts.login;

import com.nicknathanjustin.streamercontracts.users.TwitchUser;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull private final UserService userService;

    @Override
    public void logUserIn(@NonNull final OAuth2Authentication auth) {
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(auth);
        final Optional<UserModel> optionalUser = userService.getUser(twitchUser.getDisplayName());
        final UserModel userModel = optionalUser.orElse(null);
        if (userModel != null) {
            userService.login(userModel);
        } else {
            userService.createUser(twitchUser.getDisplayName());
        }
    }
}
