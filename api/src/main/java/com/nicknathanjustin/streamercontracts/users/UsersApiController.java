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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    public ResponseEntity<?> privateUser(@NonNull final HttpServletRequest httpServletRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
        }

        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        final TwitchUser twitchUser = userService.getTwitchUserFromRequest(httpServletRequest);
        final long openContracts = contractService.countByStateAndStreamer(ContractState.OPEN, userModel);
        final long activeContracts = contractService.countByStateAndStreamer(ContractState.ACTIVE, userModel);
        final long declinedContracts = contractService.countByStateAndStreamer(ContractState.DECLINED, userModel);
        final long expiredContracts = contractService.countByStateAndStreamer(ContractState.EXPIRED, userModel);
        final long completedContracts = contractService.countByStateAndStreamer(ContractState.COMPLETED, userModel);
        final long failedContracts = contractService.countByStateAndStreamer(ContractState.FAILED, userModel);
        final long disputedContracts = contractService.countByStateAndStreamer(ContractState.DISPUTED, userModel);
        final BigDecimal moneyEarned = contractService.getMoneyForStreamerAndState(userModel, ContractState.COMPLETED);
        final PrivateUser privateUser = new PrivateUser(
                twitchUser,
                userModel,
                openContracts,
                activeContracts,
                declinedContracts,
                expiredContracts,
                completedContracts,
                failedContracts,
                disputedContracts,
                moneyEarned);

        return ResponseEntity.ok(privateUser);
    }

    @RequestMapping(path = "/username/{twitchUsername}", method = RequestMethod.GET)
    public ResponseEntity<?> publicUser(@PathVariable("twitchUsername") @NonNull final String twitchUsername) {
        final TwitchUser twitchUser = twitchService.getTwitchUserFromUsername(twitchUsername);
        final Optional<UserModel> optionalUserModel = userService.getUser(twitchUsername);
        final UserModel userModel = optionalUserModel.orElse(null);

        if (twitchUser != null && userModel != null) {
            final UserSettingsModel userSettingsModel = userSettingsService.getUserSettings(userModel).orElse(null);
            final PublicUser publicUser = new PublicUser(twitchUser, userSettingsModel);
            return ResponseEntity.ok(publicUser);
        }

        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/twitchId/{twitchId}", method = RequestMethod.GET)
    public ResponseEntity<?> extensionUser(@NonNull final HttpServletRequest httpServletRequest, @PathVariable("twitchId") @NonNull final String twitchId) {
        TwitchUser loggedInUser;
        boolean foundTwitchUser = false;
        try {
            loggedInUser = userService.getTwitchUserFromRequest(httpServletRequest);
            foundTwitchUser = true;
        } catch (IllegalStateException e) {
            loggedInUser = null;
        }

        final TwitchUser requestedUser = twitchService.getTwitchUserFromTwitchUserId(twitchId);

        if (!foundTwitchUser || !loggedInUser.getExternalId().equals(twitchId)) {
            // get public user
            final Optional<UserModel> optionalUserModel = userService.getUser(requestedUser.getDisplayName());
            final UserModel userModel = optionalUserModel.orElse(null);

            if (requestedUser != null && userModel != null) {
                final UserSettingsModel userSettingsModel = userSettingsService.getUserSettings(userModel).orElse(null);
                final PublicUser publicUser = new PublicUser(requestedUser, userSettingsModel);
                return ResponseEntity.ok(publicUser);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            // get private user
            final Optional<UserModel> optionalUserModel = userService.getUser(loggedInUser.getDisplayName());
            final UserModel userModel = optionalUserModel.orElse(null);

            if (userModel != null) {
                final long openContracts = contractService.countByStateAndStreamer(ContractState.OPEN, userModel);
                final long activeContracts = contractService.countByStateAndStreamer(ContractState.ACTIVE, userModel);
                final long declinedContracts = contractService.countByStateAndStreamer(ContractState.DECLINED, userModel);
                final long expiredContracts = contractService.countByStateAndStreamer(ContractState.EXPIRED, userModel);
                final long completedContracts = contractService.countByStateAndStreamer(ContractState.COMPLETED, userModel);
                final long failedContracts = contractService.countByStateAndStreamer(ContractState.FAILED, userModel);
                final long disputedContracts = contractService.countByStateAndStreamer(ContractState.DISPUTED, userModel);
                final BigDecimal moneyEarned = contractService.getMoneyForStreamerAndState(userModel, ContractState.COMPLETED);

                final PrivateUser privateUser = new PrivateUser(
                        loggedInUser,
                        userModel,
                        openContracts,
                        activeContracts,
                        declinedContracts,
                        expiredContracts,
                        completedContracts,
                        failedContracts,
                        disputedContracts,
                        moneyEarned);
                return ResponseEntity.ok(privateUser);
            } else {
                final UserModel newUser = userService.createUser(loggedInUser.getDisplayName(), loggedInUser.getExternalId());
                final long openContracts = contractService.countByStateAndStreamer(ContractState.OPEN, newUser);
                final long activeContracts = contractService.countByStateAndStreamer(ContractState.ACTIVE, newUser);
                final long declinedContracts = contractService.countByStateAndStreamer(ContractState.DECLINED, newUser);
                final long expiredContracts = contractService.countByStateAndStreamer(ContractState.EXPIRED, newUser);
                final long completedContracts = contractService.countByStateAndStreamer(ContractState.COMPLETED, newUser);
                final long failedContracts = contractService.countByStateAndStreamer(ContractState.FAILED, newUser);
                final long disputedContracts = contractService.countByStateAndStreamer(ContractState.DISPUTED, newUser);
                final BigDecimal moneyEarned = contractService.getMoneyForStreamerAndState(newUser, ContractState.COMPLETED);

                final PrivateUser privateUser = new PrivateUser(
                        loggedInUser,
                        newUser,
                        openContracts,
                        activeContracts,
                        declinedContracts,
                        expiredContracts,
                        completedContracts,
                        failedContracts,
                        disputedContracts,
                        moneyEarned);
                return ResponseEntity.ok(privateUser);
            }
        }
    }

    @RequestMapping(path = "list/{page}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<UserModel>> listUsers(
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable final int page,
            @PathVariable final int pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(userService.listUsers(pageable));
    }
}
