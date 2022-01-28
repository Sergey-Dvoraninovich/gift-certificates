package com.epam.esm.service.impl;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.RefreshTokenException;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.JwtService;
import com.epam.esm.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.epam.esm.exception.RefreshTokenException.State.INVALID_TOKEN;
import static com.epam.esm.exception.RefreshTokenException.State.TOKEN_EXPIRED;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${refreshTokenExpirationDays}")
    private Long refreshTokenDaysExpiration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId, User.class));
        refreshToken.setUser(user);
        refreshToken.setExpirationDate(Instant.now().plus(refreshTokenDaysExpiration, DAYS));
        refreshToken.setToken(generateRefreshToken());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    @Transactional
    public TokenDto refreshToken(String refreshTokenLine) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findRefreshTokenByToken(refreshTokenLine);
        if (optionalRefreshToken.isEmpty()) {
            throw new RefreshTokenException(INVALID_TOKEN);
        }
        RefreshToken refreshToken = optionalRefreshToken.get();

        if (refreshToken.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(TOKEN_EXPIRED);
        }

        User user = refreshToken.getUser();

        refreshTokenRepository.delete(refreshToken);
        refreshToken = createRefreshToken(user.getId());

        String jwt = jwtService.generateJwtToken(user.getLogin());

        return TokenDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private String generateRefreshToken() {

        return UUID.randomUUID().toString();
    }
}
