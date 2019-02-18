package com.nicknathanjustin.streamercontracts.settings;

import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.settings.requests.UpdateUserSettingsRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/userSettings")
@RequiredArgsConstructor
public class UserSettingsApiController {

    @NonNull private final SecurityService SecurityService;
    @NonNull private final UserService userService;
    @NonNull private final UserSettingsService userSettingsService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> updateUserSettings(@NonNull final HttpServletRequest httpServletRequest,
                                             @RequestBody @NonNull final UpdateUserSettingsRequest updateUserSettingsRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
        }

        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        userSettingsService.updateUserSettings(userModel, updateUserSettingsRequest);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserSettings(@NonNull final HttpServletRequest httpServletRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
        }

        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        final Optional<UserSettingsModel> optionalUserSettingsModel = userSettingsService.getUserSettings(userModel);
        final UserSettingsModel userSettingsModel = optionalUserSettingsModel.orElse(null);

        if (userSettingsModel != null) {
            final UserSettingsDto userSettingsDto = new UserSettingsDto(userSettingsModel);
            return ResponseEntity.ok(userSettingsDto);
        }

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
