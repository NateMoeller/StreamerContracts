package com.nicknathanjustin.streamercontracts.twitch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch")
@RequiredArgsConstructor
public class TwitchApiController {

    @NonNull private final TwitchService twitchService;

    @RequestMapping(path = "/topGames", method = RequestMethod.GET)
    public ResponseEntity<?> getTopGames() {
        return ResponseEntity.ok(twitchService.getTopGames());
    }

    @RequestMapping(path = "/game/{gameName}", method = RequestMethod.GET)
    public ResponseEntity<?> getGame(@PathVariable("gameName") @NonNull final String gameName) {
        return ResponseEntity.ok(twitchService.getGame(gameName));
    }
}
