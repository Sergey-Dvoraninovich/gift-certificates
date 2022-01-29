package com.epam.esm.validator;

import com.epam.esm.service.TokenProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.epam.esm.validator.JwtTokenValidationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {
    private static final String TEST_JWT_SECRET = "jkj2qkm45pkm6";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaW5keV9hZG1pbiIsImlhdCI6MTY0MTg5MTU4OCwiZXhwIjoxNjQxODk3NTg4fQ.8xKo152dr_B6NkQUUSGwr1McmznEam-DXtbkW8ONnFj4v_9i1nO7sT2IEHrH54xM6ubcpG-4culhDR8VgoL_lw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.INVALID_TOKEN.OiJjaW5keV9hZG1pbiIsImlhdCI6MTY0MTg5MTU4OCwiZXhwIjoxNjQxODk3NTg4fQ.8xKo152dr_B6NkQUUSGwr1McmznEam-DXtbkW8ONnFj4v_9i1nO7sT2IEHrH54xM6ubcpG-4culhDR8VgoL_lw";
    private static final String INVALID_SIGNATURE_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaW5keV9hZG1pbiIsImlhdCI6MTY0MTg5MTU4OCwiZXhwIjoxNjQxODk3NTg4fQ.SIGNATURE_B6NkQUUSGwr1McmznEam-DXtbkW8ONnFj4v_9i1nO7sT2IEHrH54xM6ubcpG-4culhDR8VgoL_lw";;

    @InjectMocks
    private JwtTokenValidator tokenValidator;

    @Mock
    private TokenProperties tokenProperties;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(JwtTokenValidatorTest.class);
    }

    @Test
    void testJwtInvalid() {
        when(tokenProperties.getJwtSecret()).thenReturn(TEST_JWT_SECRET);

        Optional<JwtTokenValidationError> validationError = tokenValidator.validateJwtToken(INVALID_TOKEN);

        assertTrue(validationError.isPresent());
        assertEquals(INVALID_JWT, validationError.get());
    }

    @Test
    void testJwtExpired() {
        when(tokenProperties.getJwtSecret()).thenReturn(TEST_JWT_SECRET);

        Optional<JwtTokenValidationError> validationError = tokenValidator.validateJwtToken(EXPIRED_TOKEN);

        assertTrue(validationError.isPresent());
        assertEquals(JWT_EXPIRED, validationError.get());
    }

    @Test
    void testJwtInvalidSignature() {
        when(tokenProperties.getJwtSecret()).thenReturn(TEST_JWT_SECRET);

        Optional<JwtTokenValidationError> validationError = tokenValidator.validateJwtToken(INVALID_SIGNATURE_TOKEN);

        assertTrue(validationError.isPresent());
        assertEquals(INVALID_JWT_SIGNATURE, validationError.get());
    }
}
