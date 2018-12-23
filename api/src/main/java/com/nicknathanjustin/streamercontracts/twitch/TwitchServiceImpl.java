package com.nicknathanjustin.streamercontracts.twitch;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class TwitchServiceImpl implements TwitchService {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @Override
    public Map<String, List<Map<String, Object>>> getTopGames() {
        final int numGames = 100;
        final ResponseEntity<Map> response = queryTwitch("https://api.twitch.tv/helix/games/top?first=" + numGames);
        return response.getBody();
    }

    @Override
    public Optional<TwitchUser> getTwitchUserFromUsername(@NonNull final String twitchUsername) {
        final ResponseEntity<Map> response = queryTwitch(userInfoUri + "?login=" + twitchUsername);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        return TwitchUser.createTwitchUser(details);
    }

    @Override
    public Optional<TwitchUser> getTwitchUserFromTwitchUserId(@NonNull final String twitchUserId) {
        final ResponseEntity<Map> response = queryTwitch(userInfoUri + "?id=" + twitchUserId);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        return TwitchUser.createTwitchUser(details);
    }

    private ResponseEntity<Map> queryTwitch(@NonNull final String url) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-ID", clientId);
        final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        final RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    }
}
