package com.epam.esm.exception;

import com.epam.esm.validator.JwtTokenValidationError;

public class JwtTokenException extends RuntimeException {
    private final JwtTokenValidationError validationError;

    public JwtTokenException(JwtTokenValidationError validationError) {
        this.validationError = validationError;
    }

    public JwtTokenValidationError getState() {
        return validationError;
    }
}
