package com.nicknathanjustin.streamercontracts.twitch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class TwitchServiceImpl implements TwitchService {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Override
    public Map<String, List<Map<String, Object>>> getTopGames() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-ID", clientId);
        final HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        final RestTemplate restTemplate = new RestTemplate();
        final int numGames = 100;
        final String url = "https://api.twitch.tv/helix/games/top?first=" + numGames;
        final ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        final Map<String, List<Map<String, Object>>> details = response.getBody();

        return details;
    }
}
