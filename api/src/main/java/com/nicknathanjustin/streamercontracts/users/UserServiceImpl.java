package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
    public TwitchUser getTwitchUserFromUsername(@NonNull String twitchUsername) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-ID", clientId);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        RestTemplate restTemplate = new RestTemplate();
        String url = userInfoUri + "?login=" + twitchUsername;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        final List<Map<String, Object>> data = details.get("data");

        if (data.size() <= 0) {
            return null;
        }
        final Map<String, Object> properties = data.get(0);

        return new TwitchUser(
                (String) properties.get("login"),
                (String) properties.get("display_name"),
                (String) properties.get("type"),
                (String) properties.get("broadcaster_type"),
                (String) properties.get("description"),
                (String) properties.get("profile_image_url"),
                (String) properties.get("offline_image_url"),
                (int) properties.get("view_count")
        );
    }
}