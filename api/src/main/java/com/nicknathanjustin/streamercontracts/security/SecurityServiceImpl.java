package com.nicknathanjustin.streamercontracts.security;

import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    @NonNull private final UserService userService;

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;

    @Value("${twitch.whiteListedAccounts}")
    private List<String> whiteListedAccounts;

    @Value("${application.blackListedAccounts}")
    private List<String> blackListedAccounts;

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
        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        if (isEnvLockedToWhiteListedAccounts()) {
            return !isUserWhiteListed(userModel);
        }
        return isUserBlackListed(userModel);
    }

    private boolean isEnvLockedToWhiteListedAccounts() {
        // Beta endpoint is currently open to any client. However, we want to only allow bountyStreamer accounts to
        // access Beta. As such, we maintain a white list of accounts that are allowed to authenticate with Beta.
        return environment.equals("Beta");
    }

    private boolean isUserWhiteListed(@NonNull final UserModel userModel) {
        return whiteListedAccounts.contains(userModel.getTwitchUsername());
    }

    private boolean isUserBlackListed(@NonNull final UserModel userModel) {
        return blackListedAccounts.contains(userModel.getTwitchUsername());
    }
}
