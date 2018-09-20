package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersApiController {
    
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity user(@Nullable final OAuth2Authentication auth) {
        if (auth != null) {
            final TwitchUser twitchUser = TwitchUser.createTwitchUser(auth);
            final UserModel userModel = userService.getUser(twitchUser.getDisplayName());
            final UserDto userDto = new UserDto(twitchUser, userModel);
            return ResponseEntity.ok(userDto);
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}
