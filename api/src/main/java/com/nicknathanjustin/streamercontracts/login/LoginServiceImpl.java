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

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    @NonNull private final UserService userService;

    @Override
    public void logUserIn(@NonNull final OAuth2Authentication auth) {
        final Authentication userAuth = auth.getUserAuthentication();
        final Map<String, List<Map<String, Object>>> authDetails = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(authDetails);
        
        if (twitchUser == null) {
            throw new IllegalStateException("unable to retrieve TwitchUser.");
        }
        
        final UserModel userModel = userService.getUser(twitchUser.getDisplayName()).orElse(null);
        if (userModel != null) {
            userService.login(userModel);
        } else {
            userService.createUser(twitchUser.getDisplayName(), twitchUser.getExternalId());
        }
    }
}
