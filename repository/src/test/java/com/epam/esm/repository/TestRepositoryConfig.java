package com.epam.esm.repository;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan("com.epam.esm.entity")
@EnableJpaRepositories("com.epam.esm.repository")
public class TestRepositoryConfig {

}
