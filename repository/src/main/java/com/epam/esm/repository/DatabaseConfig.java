package com.epam.esm.repository;

import lombok.Data;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@Profile("!test")
@EnableTransactionManagement
@ConfigurationProperties(prefix = "db")
@Data
public class DatabaseConfig {

    private String driver;
    private String url;
    private String username;
    private String password;
    private String minPoolSize;
    private String maxPoolSize;

    @Bean
    public DataSource mysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(Integer.parseInt(minPoolSize));
        dataSource.setMaxTotal(Integer.parseInt(maxPoolSize));

//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS in_memory_certificates\\\\; \\\n" +
//                "        RUNSCRIPT FROM 'classpath:init_schema.sql'");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        dataSource.setMinIdle(Integer.parseInt("2"));
//        dataSource.setMaxTotal(Integer.parseInt("2"));

        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
