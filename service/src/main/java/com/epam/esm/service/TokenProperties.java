package com.epam.esm.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class TokenProperties {
    private String jwtSecret;
    private Long accessTokenExpirationMinutes;
    private Long refreshTokenExpirationDays;
}
