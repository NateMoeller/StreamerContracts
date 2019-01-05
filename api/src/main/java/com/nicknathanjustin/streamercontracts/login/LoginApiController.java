package com.nicknathanjustin.streamercontracts.login;

import com.nicknathanjustin.streamercontracts.security.SecurityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginApiController {

    @NonNull private final LoginService loginService;
    @NonNull private final SecurityService securityService;

    @Value("${application.frontEndUrl}")
    private String frontEndUrl;

    @RequestMapping(method = RequestMethod.GET)
    public void ping(@NonNull final HttpServletRequest request,
                     @NonNull final HttpServletResponse response,
                     @Nullable final OAuth2Authentication auth) throws IOException {

        // Workaround for Oauth2 routing. See this SO post: https://stackoverflow.com/questions/34692528/spring-security-oauth2-dance-and-get-parameters
        // Long story short is that there isnt a nice way to move redirects away from "/" when using @EnableOAuth2Sso
        // Since its difficult to move the redirect, we'll have "/" function as the OauthSuccessRedirect when we see
        // a request from our Oauth login flow
        if(isOauthSuccessRedirect(auth, request)) {
            if (!securityService.isAnonymousRequest(request)) {
                loginService.logUserIn(auth);
            }
            response.sendRedirect(frontEndUrl + "/profile");
        }
    }

    private boolean isOauthSuccessRedirect(@Nullable final OAuth2Authentication auth, @NonNull final HttpServletRequest request) {
        final String refererHeader = request.getHeader("referer");
        return auth != null &&
                refererHeader != null &&
                (refererHeader.contains(frontEndUrl + "/login") || refererHeader.contains("passport.twitch.tv") || refererHeader.contains("id.twitch.tv"));
    }
}
