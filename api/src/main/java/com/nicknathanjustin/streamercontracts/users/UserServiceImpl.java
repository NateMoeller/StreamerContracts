package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @NonNull final UserModelRepository userModelRepository;

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;

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
    public Optional<TwitchUser> getTwitchUserFromUsername(@NonNull final String twitchUsername) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-ID", clientId);
        final HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        final RestTemplate restTemplate = new RestTemplate();
        final String url = userInfoUri + "?login=" + twitchUsername;
        final ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        Optional<TwitchUser> optionalTwitchUser = TwitchUser.createTwitchUser(details);

        return optionalTwitchUser;
    }
}