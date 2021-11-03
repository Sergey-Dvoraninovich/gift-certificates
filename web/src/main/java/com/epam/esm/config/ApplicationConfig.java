package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
public class ApplicationConfig {
    private static final String ERROR_MESSAGES_BUNDLE_NAME = "internationalization/error_messages";
    private static final String DEVELOPMENT_PROFILE = "development";
    private static final String PRODUCTION_PROFILE = "production";

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(ERROR_MESSAGES_BUNDLE_NAME);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, DEVELOPMENT_PROFILE);

        return messageSource;
    }
}
