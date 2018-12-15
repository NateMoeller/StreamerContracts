package com.nicknathanjustin.streamercontracts.twitch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch")
@RequiredArgsConstructor
@Slf4j
public class TwitchApiController {

    @NonNull
    private final TwitchService twitchService;

    @RequestMapping(path = "/topGames", method = RequestMethod.GET)
    public ResponseEntity getTopGames() {
        return ResponseEntity.ok(twitchService.getTopGames());
    }
}
