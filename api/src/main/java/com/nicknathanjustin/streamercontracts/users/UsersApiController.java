package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.dtos.PrivateUser;
import com.nicknathanjustin.streamercontracts.users.dtos.PublicUser;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersApiController {
    
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity privateUser(@Nullable final OAuth2Authentication auth) {
        if (auth != null) {
            final Authentication userAuth = auth.getUserAuthentication();
            final Map<String, List<Map<String, Object>>> authDetails = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
            final Optional<TwitchUser> optionalTwitchUser = TwitchUser.createTwitchUser(authDetails);
            final TwitchUser twitchUser = optionalTwitchUser.orElse(null);
            if (twitchUser == null) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            final Optional<UserModel> optionalUserModel = userService.getUser(twitchUser.getDisplayName());
            final UserModel userModel = optionalUserModel.orElse(null);
            if(userModel != null) {
                final PrivateUser privateUser = new PrivateUser(twitchUser, userModel);
                return ResponseEntity.ok(privateUser);
            }
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/{twitchUsername}", method = RequestMethod.GET)
    public ResponseEntity<PublicUser> publicUser(@PathVariable("twitchUsername") @NonNull final String twitchUsername) {
        final Optional<TwitchUser> optionalTwitchUser = userService.getTwitchUserFromUsername(twitchUsername);
        final Optional<UserModel> optionalUserModel = userService.getUser(twitchUsername);
        final UserModel userModel = optionalUserModel.orElse(null);
        final TwitchUser twitchUser = optionalTwitchUser.orElse(null);

        if (twitchUser != null && userModel != null) {
            final PublicUser publicUser = new PublicUser(twitchUser);
            return ResponseEntity.ok(publicUser);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
