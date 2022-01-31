package com.epam.esm.service.impl;

import com.epam.esm.service.JwtService;
import com.epam.esm.service.TokenProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final int MILLISECONDS_IN_MINUTE = 60000;

    private final TokenProperties tokenProperties;

    public String generateJwtToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenProperties.getAccessTokenExpirationMinutes() * MILLISECONDS_IN_MINUTE))
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getJwtSecret())
                .compact();
    }

    public String getLoginFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenProperties.getJwtSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}