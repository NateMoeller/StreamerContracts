package com.nicknathanjustin.streamercontracts.security;

import com.nicknathanjustin.streamercontracts.users.UserService;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    @NonNull private final UserService userService;

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;

    @Value("${twitch.whiteListedAccounts}")
    private String[] whiteListedAccounts;

    @Value("${application.blackListedAccounts}")
    private String[] blackListedAccounts;

    @Value("${application.environment}")
    private String environment;

    @Override
    public boolean isAnonymousRequest(@NonNull final HttpServletRequest httpServletRequest) {
        final String jwtToken = httpServletRequest.getHeader(jwtHeader);
        final Principal principal = httpServletRequest.getUserPrincipal();
        final boolean isUserRecognized = (jwtToken != null || principal instanceof OAuth2Authentication);

        if (isUserRecognized) {
            return isRecognizedUserBlocked(httpServletRequest);
        }
        return true;
    }

    private boolean isRecognizedUserBlocked(@NonNull final HttpServletRequest httpServletRequest) {
        final TwitchUser twitchUser = userService.getTwitchUserFromRequest(httpServletRequest);
        if (isEnvLockedToWhiteListedAccounts()) {
            return !isUserWhiteListed(twitchUser);
        }
        return isUserBlackListed(twitchUser);
    }

    private boolean isEnvLockedToWhiteListedAccounts() {
        // Beta endpoint is currently open to any client. However, we want to only allow bountyStreamer accounts to
        // access Beta. As such, we maintain a white list of accounts that are allowed to authenticate with Beta.
        return environment.equals("Beta");
    }

    private boolean isUserWhiteListed(@NonNull final TwitchUser twitchUser) {
        return Arrays.asList(whiteListedAccounts).contains(twitchUser.getDisplayName());
    }

    private boolean isUserBlackListed(@NonNull final TwitchUser twitchUser) {
        return Arrays.asList(blackListedAccounts).contains(twitchUser.getDisplayName());
    }
}
