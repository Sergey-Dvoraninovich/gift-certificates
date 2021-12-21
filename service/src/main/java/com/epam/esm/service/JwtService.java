package com.epam.esm.service;

/**
 * The interface JWT service.
 */
public interface JwtService {
    /**
     * Generate JWT.
     *
     * @param login the User login
     * @return the string of JWT
     */
    String generateJwtToken(String login);

    /**
     * Gets login from JWT.
     *
     * @param token the token
     * @return the User login from JWT
     */
    String getLoginFromJwtToken(String token);
}
