package com.nicknathanjustin.streamercontracts.users;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UsersApiController {

    @Value("${frontEndUrl}")
    private String frontEndUrl;

    @NonNull private final UserService userService;

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public Principal user(@NonNull final Principal principal) {
        return principal;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void registerUserIfNotExists(@NonNull final HttpServletResponse response, @NonNull final OAuth2Authentication auth) throws IOException {
        final TwitchUser twitchUser = TwitchUser.createTwitchUser(auth);
        final UserModel user = userService.getUser(twitchUser.getDisplayName());
        if (user != null) {
            userService.login(user);
        }
        else {
            userService.createUser(twitchUser.getDisplayName());
        }

        response.sendRedirect(frontEndUrl + "/profile");
    }
}
