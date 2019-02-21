package com.nicknathanjustin.streamercontracts.twitch;

import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
import com.nicknathanjustin.streamercontracts.twitch.dtos.Game;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TwitchService {

    /**
     * Get the top games from the twitch api
     *
     * @return A Map with 2 fields, data and pagination.
     * data contains a list of games, and pagination contains a cursor value to specify the starting point of the next set of results.
     */
    Map<String, List<Map<String, Object>>> getTopGames();

    /**
     * Get the a game from the twitch api
     *
     * @return A game object
     */
    Game getGame(String gameName);

    /**
     * Gets the TwitchUser object from Twitch API
     *
     * @param twitchUsername The username of the twitch user
     * @return An Optional<TwitchUser> for the user
     */
    TwitchUser getTwitchUserFromUsername(String twitchUsername);

    /**
     * Gets the TwitchUser object from Twitch API
     *
     * @param twitchUserId The id of the twitch user
     * @return An Optional<TwitchUser> for the user
     */
    TwitchUser getTwitchUserFromTwitchUserId(String twitchUserId);

    /**
     * Sends the new/updated contract to the extension for real-time updating
     * @param contractState the state to refresh
     * @param channelId the channel to send the bounty to
     */
    ResponseEntity sendExtensionRefresh(ContractState contractState, String channelId);

    /**
     * @param user the user to send the message to
     * @param contract the bounty to be shown
     */
    ResponseEntity sendExtensionActivateOverlay(UserModel user, Contract contract);

    /**
     * @param user the user to send the message to
     * @param contract the bounty to be deactivated
     */
    ResponseEntity sendExtensionDeactivateOverlay(UserModel user, Contract contract);
}
