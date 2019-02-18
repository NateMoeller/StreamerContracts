package com.nicknathanjustin.streamercontracts.users;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_ID_CLAIM_KEY = "user_id";

    @NonNull final TwitchService twitchService;
    @NonNull final UserModelRepository userModelRepository;

    @Value("${twitch.extension.secret}")
    private String jwtSigningSecret;

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;

    @Override
    public UserModel createUser(@NonNull final String twitchUsername, @NonNull final String twitchId) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        return userModelRepository.save(UserModel.builder()
                .twitchUsername(twitchUsername)
                .twitchId(twitchId)
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
    public Page<UserModel> listUsers(@NonNull final Pageable pageable) {
        return userModelRepository.findAll(pageable);
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
    public UserModel getUserModelFromRequest(@NonNull final HttpServletRequest httpServletRequest) {
        final TwitchUser twitchUser = getTwitchUserFromRequest(httpServletRequest);
        final UserModel userModel = getUser(twitchUser.getDisplayName()).orElse(null);
        if (userModel == null) {
            throw new IllegalStateException("unable to retrieve " + UserModel.class.getName() + " from authentication object: " + httpServletRequest);
        }
        return userModel;
    }

    @Override
    public TwitchUser getTwitchUserFromRequest(@NonNull final HttpServletRequest httpServletRequest) {
        final String jwtToken = httpServletRequest.getHeader(jwtHeader);
        final Principal principal = httpServletRequest.getUserPrincipal();
        if (jwtToken != null) {
            return getTwitchUserFromJwtToken(jwtToken);
        } else if (principal instanceof OAuth2Authentication){
            return getTwitchUserFromOAuth((OAuth2Authentication) principal);
        } else {
            throw new IllegalStateException("No jwtToken or OAuth2Authentication associated with httpServletRequest: " + httpServletRequest);
        }
    }

    private TwitchUser getTwitchUserFromOAuth(@NonNull final OAuth2Authentication oAuth2Authentication) {
        final Authentication userAuth = oAuth2Authentication.getUserAuthentication();
        final Map<String, List<Map<String, Object>>> authDetails = (Map<String, List<Map<String, Object>>>) userAuth.getDetails();
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(authDetails);
        if (twitchUser == null) {
            throw new IllegalStateException("unable to retrieve " + TwitchUser.class.getName() + " from oAuth2Authentication: " + oAuth2Authentication);
        }
        return twitchUser;
    }

    private TwitchUser getTwitchUserFromJwtToken(@NonNull final String jwtToken) {
        final String twitchUserId = getTwitchUserIdFromJwtToken(jwtToken);
        final TwitchUser twitchUser = twitchService.getTwitchUserFromTwitchUserId(twitchUserId);
        if (twitchUser == null) {
            throw new IllegalStateException("unable to retrieve " + TwitchUser.class.getName() + " with twitchUserId: " + twitchUserId);
        }
        return twitchUser;
    }

    private String getTwitchUserIdFromJwtToken(@NonNull final String jwtToken) throws JWTVerificationException{
        final String token = jwtToken.split(" ")[1];
        final byte[] decodedSecret = Base64.getDecoder().decode(jwtSigningSecret);
        final SecretKey key = Keys.hmacShaKeyFor(decodedSecret);
        final Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        final Claims claims = jws.getBody();
        if (!claims.containsKey(USER_ID_CLAIM_KEY)) {
            throw new IllegalStateException("Claim: " + USER_ID_CLAIM_KEY + " does not exist for decoded jwtToken: " + jws);
        }
        return claims.get(USER_ID_CLAIM_KEY, String.class);
    }
}