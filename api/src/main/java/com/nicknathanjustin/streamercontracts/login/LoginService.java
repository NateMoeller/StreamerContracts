package com.nicknathanjustin.streamercontracts.login;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface LoginService {

    void logUserIn(OAuth2Authentication auth);
}
