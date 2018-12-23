package com.nicknathanjustin.streamercontracts.security;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class SecurityServiceImpl implements SecurityService {

    @Value("${twitch.extension.jwtHeader}")
    private String jwtHeader;

    @Override
    public boolean isAnonymousRequest(@NonNull final HttpServletRequest httpServletRequest) {
        final String jwtToken = httpServletRequest.getHeader(jwtHeader);
        final Principal principal = httpServletRequest.getUserPrincipal();
        return jwtToken == null && principal == null;
    }
}
