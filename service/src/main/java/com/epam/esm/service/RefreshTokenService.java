package com.epam.esm.service;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.entity.RefreshToken;

/**
 * The interface Refresh Token service.
 */
public interface RefreshTokenService {
    /**
     *Create Refresh Token.
     *
     * @param userId the User id
     * @return the Refresh Token
     */
    RefreshToken createRefreshToken(long userId);

    /**
     *Refresh token.
     *
     * @param refreshToken the Refresh Token
     * @return the Token Dto
     */
    TokenDto refreshToken(String refreshToken);
}
