package com.nicknathanjustin.streamercontracts.twitch;

import java.util.List;
import java.util.Map;

public interface TwitchService {

    /**
     * Get the top games from the twitch api
     *
     * @return A List of top games
     */
    Map<String, List<Map<String, Object>>> getTopGames();
}
