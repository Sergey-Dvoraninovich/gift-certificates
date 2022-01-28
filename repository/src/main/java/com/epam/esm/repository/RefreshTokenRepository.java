package com.epam.esm.repository;

import com.epam.esm.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Refresh Token repository.
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    /**
     * Find Refresh Token by token optional.
     *
     * @param token the token
     * @return the optional Refresh Token
     */
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    /**
     * Save Refresh Token.
     *
     * @param refreshToken the Refresh Token
     * @return the optional Refresh Token
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Delete Refresh Token.
     *
     * @param refreshToken the Refresh Token
     */
    void delete(RefreshToken refreshToken);
}
