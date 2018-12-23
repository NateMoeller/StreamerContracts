package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.settings.UserSettingsModel;
import com.nicknathanjustin.streamercontracts.settings.UserSettingsService;
import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import com.nicknathanjustin.streamercontracts.users.dtos.PrivateUser;
import com.nicknathanjustin.streamercontracts.users.dtos.PublicUser;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersApiController {

    @NonNull private final ContractService contractService;
    @NonNull private final SecurityService SecurityService;
    @NonNull private final TwitchService twitchService;
    @NonNull private final UserService userService;
    @NonNull private final UserSettingsService userSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity privateUser(@NonNull final HttpServletRequest httpServletRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        final TwitchUser twitchUser = userService.getTwitchUserFromRequest(httpServletRequest);
        final long openContracts = contractService.countByStateAndStreamer(ContractState.OPEN, userModel);
        final long acceptedContracts = contractService.countByStateAndStreamer(ContractState.ACCEPTED, userModel);
        final long declinedContracts = contractService.countByStateAndStreamer(ContractState.DECLINED, userModel);
        final long expiredContracts = contractService.countByStateAndStreamer(ContractState.EXPIRED, userModel);
        final long completedContracts = contractService.countByStateAndStreamer(ContractState.COMPLETED, userModel);
        final long failedContracts = contractService.countByStateAndStreamer(ContractState.FAILED, userModel);
        final long disputedContracts = contractService.countByStateAndStreamer(ContractState.DISPUTED, userModel);
        final PrivateUser privateUser = new PrivateUser(
                twitchUser,
                userModel,
                openContracts,
                acceptedContracts,
                declinedContracts,
                expiredContracts,
                completedContracts,
                failedContracts,
                disputedContracts);

        return ResponseEntity.ok(privateUser);
    }

    @RequestMapping(path = "/{twitchUsername}", method = RequestMethod.GET)
    public ResponseEntity publicUser(@PathVariable("twitchUsername") @NonNull final String twitchUsername) {
        final Optional<TwitchUser> optionalTwitchUser = twitchService.getTwitchUserFromUsername(twitchUsername);
        final Optional<UserModel> optionalUserModel = userService.getUser(twitchUsername);
        final UserModel userModel = optionalUserModel.orElse(null);
        final TwitchUser twitchUser = optionalTwitchUser.orElse(null);

        if (twitchUser != null && userModel != null) {
            final UserSettingsModel userSettingsModel = userSettingsService.getUserSettings(userModel).orElse(null);
            final PublicUser publicUser = new PublicUser(twitchUser, userSettingsModel);
            return ResponseEntity.ok(publicUser);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
