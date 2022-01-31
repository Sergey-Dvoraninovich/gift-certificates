package com.epam.esm.service;

import com.epam.esm.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private static final String TEST_JWT_SECRET = "jkj2qkm45pkm6";

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private TokenProperties tokenProperties;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(RefreshTokenService.class);
    }

    @Test
    void testGenerate() {
        String login = "Alex";
        when(tokenProperties.getAccessTokenExpirationMinutes()).thenReturn(30L);
        when(tokenProperties.getJwtSecret()).thenReturn(TEST_JWT_SECRET);

        String jwtToken = jwtService.generateJwtToken(login);

        assertNotNull(jwtToken);
    }

    @Test
    void testGetLogin() {
        String expectedLogin = "user";
        when(tokenProperties.getAccessTokenExpirationMinutes()).thenReturn(30L);
        when(tokenProperties.getJwtSecret()).thenReturn(TEST_JWT_SECRET);

        String jwtToken = jwtService.generateJwtToken(expectedLogin);
        String actualLogin = jwtService.getLoginFromJwtToken(jwtToken);

        assertEquals(expectedLogin, actualLogin);
    }
}
