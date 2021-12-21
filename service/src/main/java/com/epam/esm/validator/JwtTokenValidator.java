package com.epam.esm.validator;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.epam.esm.validator.JwtTokenValidationError.*;

@Component
public class JwtTokenValidator {

    @Value("${jwtSecret}")
    private String jwtSecret;

    public Optional<JwtTokenValidationError> validateJwtToken(String accessToken) {
        Optional<JwtTokenValidationError> validationError = Optional.empty();
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
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
