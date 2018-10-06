package com.nicknathanjustin.streamercontracts.login;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull private final UserService userService;

    @Override
    public void logUserIn(@NonNull final OAuth2Authentication auth) {
        final Authentication userAuth = auth.getUserAuthentication();
        final Map<String, List<Map<String, Object>>> authDetails = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
        final Optional<TwitchUser> optionalTwitchUser = TwitchUser.createTwitchUser(authDetails);
        final TwitchUser twitchUser = optionalTwitchUser.orElse(null);
        UserModel userModel = null;
        if(twitchUser != null) {
            final Optional<UserModel> optionalUser = userService.getUser(twitchUser.getDisplayName());
            userModel = optionalUser.orElse(null);
        }

        if (userModel != null) {
            userService.login(userModel);
        } else {
            userService.createUser(twitchUser.getDisplayName());
        }
    }
}
