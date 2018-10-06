package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.dtos.PrivateUser;
import com.nicknathanjustin.streamercontracts.users.dtos.PublicUser;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersApiController {
    
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity privateUser(@Nullable final OAuth2Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final TwitchUser twitchUser = userService.getTwitchUserFromAuthContext(authentication);
        final UserModel userModel = userService.getUserFromAuthContext(authentication);
        final PrivateUser privateUser = new PrivateUser(twitchUser, userModel);
        return ResponseEntity.ok(privateUser);
    }

    @RequestMapping(path = "/{twitchUsername}", method = RequestMethod.GET)
    public ResponseEntity publicUser(@PathVariable("twitchUsername") @NonNull final String twitchUsername) {
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
