package com.nicknathanjustin.streamercontracts.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String USER_ID_CLAIM_KEY = "user_id";

    @NonNull final TwitchService twitchService;
    @NonNull final UserModelRepository userModelRepository;

    @Value("${twitch.extension.secret}")
    private String jwtSigningSecret;

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;

    @Override
    public UserModel createUser(@NonNull final String twitchUsername) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        return userModelRepository.save(UserModel.builder()
                .twitchUsername(twitchUsername)
                .totalLogins(1)
                .createdAt(creationTimestamp)
                .lastLogin(creationTimestamp)
                .build());
    }

    @Override
    public Optional<UserModel> getUser(@NonNull final String twitchUsername) {
        return Optional.ofNullable(userModelRepository.findByTwitchUsername(twitchUsername));
    }

    @Override
    public void login(@NonNull final UserModel user) {
        final Timestamp loginTimestamp = new Timestamp(System.currentTimeMillis());
        final int totalLogins = user.getTotalLogins() + 1;
        user.setTotalLogins(totalLogins);
        user.setLastLogin(loginTimestamp);
        userModelRepository.save(user);
    }

    @Override
    public UserModel getUserModelFromRequest(@NonNull final HttpServletRequest httpServletRequest) throws IllegalArgumentException {
        final TwitchUser twitchUser = getTwitchUserFromRequest(httpServletRequest);
        final UserModel userModel = getUser(twitchUser.getDisplayName()).orElse(null);
        if (userModel == null) {
            throw new IllegalArgumentException("unable to retrieve " + UserModel.class.getName() + " from authentication object: " + httpServletRequest);
        }
        return userModel;
    }

    @Override
    public TwitchUser getTwitchUserFromRequest(@NonNull final HttpServletRequest httpServletRequest) {
        final String jwtToken = httpServletRequest.getHeader(jwtHeader);
        final Principal principal = httpServletRequest.getUserPrincipal();
        if (jwtToken != null) {
            return getTwitchUserFromJwtToken(jwtToken);
        } else if (principal != null){
            return getTwitchUserFromOAuth((OAuth2Authentication) principal);
        } else {
            throw new IllegalArgumentException("No jwtToken or principal associated with httpServletRequest: " + httpServletRequest);
        }
    }

    private TwitchUser getTwitchUserFromOAuth(@NonNull final OAuth2Authentication oAuth2Authentication) {
        final Authentication userAuth = oAuth2Authentication.getUserAuthentication();
        final Map<String, List<Map<String, Object>>> authDetails = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(authDetails).orElse(null);
        if (twitchUser == null) {
            throw new IllegalArgumentException("unable to retrieve " + TwitchUser.class.getName() + " from oAuth2Authentication: " + oAuth2Authentication);
        }
        return twitchUser;
    }

    private TwitchUser getTwitchUserFromJwtToken(@NonNull final String jwtToken) {
        final String twitchUserId = getTwitchUserIdFromJwtToken(jwtToken);
        final TwitchUser twitchUser = twitchService.getTwitchUserFromTwitchUserId(twitchUserId).orElse(null);
        if (twitchUser == null) {
            throw new IllegalArgumentException("unable to retrieve " + TwitchUser.class.getName() + " with twitchUserId: " + twitchUserId);
        }
        return twitchUser;
    }

    private String getTwitchUserIdFromJwtToken(@NonNull final String jwtToken) throws JWTVerificationException{
        final String token = jwtToken.split(" ")[1];
        final byte[] decodedSecret = Base64.getDecoder().decode(jwtSigningSecret);
        final Algorithm algorithm = Algorithm.HMAC256(decodedSecret);
        final JWTVerifier verifier = JWT.require(algorithm).build();
        final DecodedJWT decodedJWT = verifier.verify(token);
        final Map<String, Claim> claims = decodedJWT.getClaims();
        if (!claims.containsKey(USER_ID_CLAIM_KEY)) {
            throw new IllegalArgumentException("Claim: " + USER_ID_CLAIM_KEY + " does not exist for decoded jwtToken: " + decodedJWT);
        }
        return claims.get(USER_ID_CLAIM_KEY).asString();
    }
}