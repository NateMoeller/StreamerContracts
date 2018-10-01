package com.nicknathanjustin.streamercontracts.users;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    public ResponseEntity privateUser(@Nullable final OAuth2Authentication auth) {
        if (auth != null) {
            final TwitchUser twitchUser = TwitchUser.createTwitchUser(auth);
            final Optional<UserModel> optionalUserModel = userService.getUser(twitchUser.getDisplayName());
            final UserModel userModel = optionalUserModel.orElse(null);
            if(userModel != null) {
                final UserDto userDto = new UserDto(twitchUser, userModel);
                return ResponseEntity.ok(userDto);
            }
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/{twitchUsername}", method = RequestMethod.GET)
    @JsonView(View.PublicUser.class)
    public ResponseEntity<UserDto> publicUser(@PathVariable("twitchUsername") @NonNull final String twitchUsername) {
        final TwitchUser twitchUser = userService.getTwitchUserFromUsername(twitchUsername);
        final Optional<UserModel> optionalUserModel = userService.getUser(twitchUsername);
        final UserModel userModel = optionalUserModel.orElse(null);

        if (twitchUser != null && userModel != null) {
            final UserDto userDto = new UserDto(twitchUser, userModel);
            return ResponseEntity.ok(userDto);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
