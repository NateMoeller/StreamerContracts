package com.nicknathanjustin.streamercontracts.login;

import com.nicknathanjustin.streamercontracts.users.TwitchUser;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull private final UserService userService;

    @Override
    public void logUserIn(@NonNull final OAuth2Authentication auth) {
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(auth);
        final UserModel user = userService.getUser(twitchUser.getDisplayName());
        if (user != null) {
            userService.login(user);
        } else {
            userService.createUser(twitchUser.getDisplayName());
        }
    }
}
