package com.nicknathanjustin.streamercontracts.settings;

import com.nicknathanjustin.streamercontracts.settings.requests.UpdateUserSettingsRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userSettings")
@RequiredArgsConstructor
@Slf4j
public class UserSettingsApiController {

    @NonNull private final UserSettingsService userSettingsService;
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateUserSettings(@Nullable final OAuth2Authentication authentication,
                                             @RequestBody @NonNull final UpdateUserSettingsRequest updateUserSettingsRequest) {
        if (authentication == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel userModel = userService.getUserFromAuthContext(authentication);
        userSettingsService.updateUserSettings(userModel, updateUserSettingsRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}
