package com.epam.esm.service;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.RefreshTokenException;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.util.Optional;

import static com.epam.esm.entity.UserRoleName.USER;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProperties tokenProperties;
    @Mock
    private JwtService jwtService;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(RefreshTokenService.class);
    }

    @Test
    void testCreateRefresh() {
        User user = provideUser();

        when(tokenProperties.getRefreshTokenExpirationDays()).thenReturn(30L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any())).thenAnswer(answer);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        assertNotNull(refreshToken);
        assertTrue(refreshToken.getId() > 0);
        assertTrue(Instant.now().isBefore(refreshToken.getExpirationDate()));
        assertNotNull(refreshToken);
    }

    @Test
    void testCreateTokenUserNotExists() {
        User user = provideUser();
        long userId = user.getId();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        try {
            refreshTokenService.createRefreshToken(userId);
            fail();
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }

    }

    @Test
    void testRefresh() {
        RefreshToken storedRefreshToken = provideStoredRefreshToken();

        when(refreshTokenRepository.findRefreshTokenByToken(storedRefreshToken.getToken()))
                .thenReturn(Optional.of(storedRefreshToken));
        doNothing().when(refreshTokenRepository).delete(storedRefreshToken);
        when(jwtService.generateJwtToken(storedRefreshToken.getUser().getLogin())).thenReturn(provideJwtToken());
        when(tokenProperties.getRefreshTokenExpirationDays()).thenReturn(30L);
        when(userRepository.findById(storedRefreshToken.getUser().getId()))
                .thenReturn(Optional.of(storedRefreshToken.getUser()));
        when(refreshTokenRepository.save(any())).thenAnswer(answer);

        TokenDto newRefreshToken = refreshTokenService.refreshToken(storedRefreshToken.getToken());

        assertNotNull(newRefreshToken);
        assertNotEquals(storedRefreshToken.getToken(), newRefreshToken.getRefreshToken());
        assertEquals(provideJwtToken(), newRefreshToken.getAccessToken());
    }

    @Test
    void testRefreshInvalidToken() {
        RefreshToken storedRefreshToken = provideStoredRefreshToken();
        String token = storedRefreshToken.getToken();

        when(refreshTokenRepository.findRefreshTokenByToken(storedRefreshToken.getToken()))
                .thenReturn(Optional.of(storedRefreshToken));

        try {
            refreshTokenService.refreshToken(token);
            fail();
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }

    }

    @Test
    void testRefreshTokenExpired() {
        RefreshToken storedRefreshToken = provideStoredRefreshToken();
        storedRefreshToken.setExpirationDate(Instant.now().minus(1L, DAYS));
        String token = storedRefreshToken.getToken();

        when(refreshTokenRepository.findRefreshTokenByToken(storedRefreshToken.getToken()))
                .thenReturn(Optional.of(storedRefreshToken));
        doNothing().when(refreshTokenRepository).delete(storedRefreshToken);

        try {
            refreshTokenService.refreshToken(token);
            fail();
        } catch (RefreshTokenException e) {
            assertTrue(true);
        }
    }

    @Test
    void testRefreshTokenNotExists() {
        RefreshToken storedRefreshToken = provideStoredRefreshToken();
        String token = storedRefreshToken.getToken();

        when(refreshTokenRepository.findRefreshTokenByToken(storedRefreshToken.getToken()))
                .thenReturn(Optional.empty());

        try {
            refreshTokenService.refreshToken(token);
            fail();
        } catch (RefreshTokenException e) {
            assertTrue(true);
        }
    }

    private Answer<RefreshToken> answer = new Answer<RefreshToken>() {
        @Override
        public RefreshToken answer(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();
            RefreshToken refreshToken = (RefreshToken) args[0];
            refreshToken.setId(1);
            return refreshToken;
        }
    };

    private User provideUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("christian_altman");
        user.setPassword("a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15");
        user.setRole(provideRole());
        user.setName("Christian");
        user.setSurname("Altman");
        user.setEmail("christian.altman@gmail.com");
        return user;
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }

    private RefreshToken provideStoredRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1);
        refreshToken.setUser(provideUser());
        refreshToken.setToken("f6f4054c-e2bf-4485-bf54-9c5f7d4c2286");
        refreshToken.setExpirationDate(Instant.now().plus(20L, DAYS));
        return refreshToken;
    }

    private String provideJwtToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaW5keV9hZG1pbiIsImlhdCI6MTY0MTg5MTU4OCwiZXhwIjoxNjQxODk3NTg4fQ.8xKo152dr_B6NkQUUSGwr1McmznEam-DXtbkW8ONnFj4v_9i1nO7sT2IEHrH54xM6ubcpG-4culhDR8VgoL_lw";
    }

}
