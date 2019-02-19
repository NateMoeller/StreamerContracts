package com.nicknathanjustin.streamercontracts.twitch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicknathanjustin.streamercontracts.twitch.dtos.Game;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class TwitchServiceImpl implements TwitchService {

    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @Value("${twitch.extension.secret}")
    private String jwtSigningSecret;

    @Override
    public Map<String, List<Map<String, Object>>> getTopGames() {
        final int numGames = 100;
        final ResponseEntity<Map> response = queryTwitch("https://api.twitch.tv/helix/games/top?first=" + numGames);
        return response.getBody();
    }

    @Override
    public Game getGame(@NonNull final String gameName) {
        final ResponseEntity<Map> response = queryTwitch("https://api.twitch.tv/helix/games?name=" + gameName);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        final List<Map<String, Object>> data = details.get("data");
        Game game = new Game();
        if(!CollectionUtils.isEmpty(data)) {
            final String boxArtUrl = (String) data.get(0).get("box_art_url");
            game = new Game(gameName, boxArtUrl);
        }

        return game;
    }

    @Override
    public TwitchUser getTwitchUserFromUsername(@NonNull final String twitchUsername) {
        final ResponseEntity<Map> response = queryTwitch(userInfoUri + "?login=" + twitchUsername);
        final Map<String, List<Map<String, Object>>> details = response.getBody();
        return TwitchUser.createTwitchUser(details);
    }

    @Override
    public TwitchUser getTwitchUserFromTwitchUserId(@NonNull final String twitchUserId) {
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

    private String getTwitchJwtToken() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final JsonNode pubsubPermissions = objectMapper.readTree("{\"send\":[\"*\"]}");
            final byte[] decodedSecret = Base64.getDecoder().decode(jwtSigningSecret);
            final SecretKey key = Keys.hmacShaKeyFor(decodedSecret);
            return Jwts.builder()
                    .claim("exp", new Date(System.currentTimeMillis() + 6000000))
                    .claim("user_id", "75481598")
                    .claim("role", "external")
                    .claim("channel_id", "75481598")
                    .claim("pubsub_perms", pubsubPermissions)
                    .signWith(key)
                    .compact();
        } catch (IOException e) {
            log.error("JSON Parsing exception while building twitch JWT token.");
            throw new RuntimeException(e);
        }
    }
}
