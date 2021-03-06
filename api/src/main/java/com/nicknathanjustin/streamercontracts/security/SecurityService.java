package com.nicknathanjustin.streamercontracts.security;

import com.auth0.jwt.exceptions.JWTVerificationException;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {

    /**
     * Checks if a HttpRequest is coming from an anonymous source.
     *
     * @param httpServletRequest The request to check.
     * @return true if request comes from an anonymous source. False otherwise.
     */
    boolean isAnonymousRequest(HttpServletRequest httpServletRequest);
    
    /**
     * Gets a user id from a JWT Token.
     *
     * @param jwtToken The JWT token.
     * @return Returns the userId from the JWT Token.
     * @throws JWTVerificationException Throws if parsing the JWT token encounters an error.
     */
    String getTwitchUserIdFromJwtToken(String jwtToken) throws JWTVerificationException;
}
