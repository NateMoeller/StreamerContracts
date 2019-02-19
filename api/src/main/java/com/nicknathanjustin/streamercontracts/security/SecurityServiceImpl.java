package com.nicknathanjustin.streamercontracts.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private static final String USER_ID_CLAIM_KEY = "user_id";

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;
    
    @Value("${twitch.extension.secret}")
    private String jwtSigningSecret;

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
        
        final boolean isUserRecognized = ((jwtToken != null && getTwitchUserIdFromJwtToken(jwtToken) != null) ||
                                           principal instanceof OAuth2Authentication);
        if (isUserRecognized) {
            return isRecognizedUserBlocked(httpServletRequest);
        }

        return true;
    }
    
    @Override
    public String getTwitchUserIdFromJwtToken(@NonNull final String jwtToken) throws JWTVerificationException {
        final String token = jwtToken.split(" ")[1];
        final byte[] decodedSecret = Base64.getDecoder().decode(jwtSigningSecret);
        final SecretKey key = Keys.hmacShaKeyFor(decodedSecret);
        final Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        final Claims claims = jws.getBody();
        if (!claims.containsKey(USER_ID_CLAIM_KEY)) {
            return null;
        }

        return claims.get(USER_ID_CLAIM_KEY, String.class);
    }


    private boolean isRecognizedUserBlocked(@NonNull final HttpServletRequest httpServletRequest) {
        //TODO: uncomment and fix circular dependency issue after twitch extension competition completes
//        final TwitchUser twitchUser = userService.getTwitchUserFromRequest(httpServletRequest);
//        if (isEnvLockedToWhiteListedAccounts()) {
//            return !isUserWhiteListed(twitchUser);
//        }
//        return isUserBlackListed(twitchUser);
        return false;
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
