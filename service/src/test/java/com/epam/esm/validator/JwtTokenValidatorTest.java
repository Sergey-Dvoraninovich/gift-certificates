package com.epam.esm.validator;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.epam.esm.validator.JwtTokenValidationError.INVALID_JWT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenValidatorTest {
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaW5keV9hZG1pbiIsImlhdCI6MTY0MTg5MTU4OCwiZXhwIjoxNjQxODk3NTg4fQ.8xKo152dr_B6NkQUUSGwr1McmznEam-DXtbkW8ONnFj4v_9i1nO7sT2IEHrH54xM6ubcpG-4culhDR8VgoL_lw";

    private JwtTokenValidator jwtTokenValidator = new JwtTokenValidator();

    @Test
    void testJwtInvalid() {
        Optional<JwtTokenValidationError> validationError = jwtTokenValidator.validateJwtToken(INVALID_TOKEN);

        assertTrue(validationError.isPresent());
        assertEquals(INVALID_JWT, validationError.get());
    }
}
