package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.users.externalusers.TwitchUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {
    /**
     * Creates the user in the database given the twitch username.
     *
     * @param  twitchUsername The username of the twitch user.
     * @param  twitchId The id of the twitch user.
     * @return An object modeling the user.
     */
    UserModel createUser(String twitchUsername, String twitchId);

    /**
     * Gets a user from the database given the twitch username.
     *
     * @param  twitchUsername The username of the twitch user.
     * @return An Optional<UserModel> for the user.
     */
    Optional<UserModel> getUser(String twitchUsername);

    /**
     * Gets a list of users.
     *
     * @param pageable identifies the page number and pagesize to retrieve
     * @return A paginated list of users.
     */
    Page<UserModel> listUsers(Pageable pageable);

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
     */
    UserModel getUserModelFromRequest(HttpServletRequest httpServletRequest);

    /**
     * Returns a TwitchUser from a valid HttpRequest Object.
     *
     * @param httpServletRequest the HttpRequest to get a user from.
     * @return TwitchUser associated with the provided HttpRequest.
     */
    TwitchUser getTwitchUserFromRequest(HttpServletRequest httpServletRequest);
}
