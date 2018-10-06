package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;

import java.util.Optional;

public interface UserService {
    /**
     * Creates the user in the database given the twitch username.
     *
     * @param  twitchUsername The username of the twitch user.
     * @return An object modeling the user.
     */
    UserModel createUser(String twitchUsername);

    /**
     * Gets a user from the database given the twitch username.
     *
     * @param  twitchUsername The username of the twitch user.
     * @return An Optional<UserModel> for the user.
     */
    Optional<UserModel> getUser(String twitchUsername);

    /**
     * Updates the login attributes of the user.
     *
     * @param  user The user model object to update.
     */
    void login(UserModel user);

    /**
     * Gets the TwitchUser object from Twitch API
     *
     * @param twitchUsername The username of the twitch user
     * @return An Optional<TwitchUser> for the user
     */
    Optional<TwitchUser> getTwitchUserFromUsername(String twitchUsername);
}
