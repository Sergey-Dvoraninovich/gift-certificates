package com.epam.esm.validator;

public enum JwtTokenValidationError {
    INVALID_JWT_SIGNATURE,
    UNSUPPORTED_JWT,
    JWT_EXPIRED,
    INVALID_JWT
}
