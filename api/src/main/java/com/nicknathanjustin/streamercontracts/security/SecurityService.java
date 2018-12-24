package com.nicknathanjustin.streamercontracts.security;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {

    /**
     * Checks if a HttpRequest is coming from an anonymous source.
     *
     * @param httpServletRequest The request to check.
     * @return true if request comes from an anonymous source. False otherwise.
     */
    public boolean isAnonymousRequest(HttpServletRequest httpServletRequest);
}
