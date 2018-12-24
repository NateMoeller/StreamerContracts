package com.nicknathanjustin.streamercontracts.twitch;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TwitchService {

    /**
     * Get the top games from the twitch api
     *
     * @return A Map with 2 fields, data and pagination.
     * data contains a list of games, and pagination contains a cursor value to specify the starting point of the next set of results.
     */
    Map<String, List<Map<String, Object>>> getTopGames();

    /**
     * Gets the TwitchUser object from Twitch API
     *
     * @param twitchUsername The username of the twitch user
     * @return An Optional<TwitchUser> for the user
     */
    Optional<TwitchUser> getTwitchUserFromUsername(String twitchUsername);

    /**
     * Gets the TwitchUser object from Twitch API
     *
     * @param twitchUserId The id of the twitch user
     * @return An Optional<TwitchUser> for the user
     */
    Optional<TwitchUser> getTwitchUserFromTwitchUserId(String twitchUserId);
}
