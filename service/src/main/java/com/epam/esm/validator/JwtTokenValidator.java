package com.epam.esm.validator;

import com.epam.esm.service.TokenProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.epam.esm.validator.JwtTokenValidationError.*;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final TokenProperties tokenProperties;

    public Optional<JwtTokenValidationError> validateJwtToken(String accessToken) {
        Optional<JwtTokenValidationError> validationError = Optional.empty();
        try {
            Jwts.parser()
                    .setSigningKey(tokenProperties.getJwtSecret())
                    .parseClaimsJws(accessToken);

        } catch (SignatureException exception) {
            validationError = Optional.of(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException exception) {
            validationError = Optional.of(JWT_EXPIRED);
        } catch (UnsupportedJwtException exception) {
            validationError = Optional.of(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException | MalformedJwtException exception) {
            validationError = Optional.of(INVALID_JWT);
        }
        return validationError;
    }
}
