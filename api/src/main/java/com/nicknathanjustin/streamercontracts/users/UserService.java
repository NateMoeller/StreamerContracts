package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;

import javax.servlet.http.HttpServletRequest;
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
     * Returns a UserModel from a valid HttpRequest Object.
     *
     * @param httpServletRequest HttpRequest to get a user from.
     * @return UserModel associated with the provided HttpRequest.
     * @throws IllegalArgumentException thrown when no user is associated with the supplied request.
     */
    UserModel getUserModelFromRequest(HttpServletRequest httpServletRequest) throws IllegalArgumentException;

    /**
     * Returns a TwitchUser from a valid HttpRequest Object.
     *
     * @param httpServletRequest the HttpRequest to get a user from.
     * @return TwitchUser associated with the provided HttpRequest.
     * @throws IllegalArgumentException thrown when no user is associated with the supplied request.
     */
    TwitchUser getTwitchUserFromRequest(HttpServletRequest httpServletRequest);
}
