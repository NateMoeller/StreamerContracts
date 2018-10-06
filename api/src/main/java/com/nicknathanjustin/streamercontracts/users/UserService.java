package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

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

    /**
     * Returns a UserModel from a valid OAuth2Authentication Object.
     *
     * @param authentication OAuth2Authentication for the signed in user.
     * @return UserModel associated with the provided OAuth2Authentication.
     * @throws IllegalArgumentException thrown when an invalid OAuth2Authentication is provided.
     */
    UserModel getUserFromAuthContext(OAuth2Authentication authentication) throws IllegalArgumentException;

    /**
     * Returns a TwitchUser from a valid OAuth2Authentication Object.
     *
     * @param authentication OAuth2Authentication for the signed in user.
     * @return TwitchUser associated with the provided OAuth2Authentication.
     * @throws IllegalArgumentException thrown when an invalid OAuth2Authentication is provided.
     */
    TwitchUser getTwitchUserFromAuthContext(OAuth2Authentication authentication) throws IllegalArgumentException;
}
