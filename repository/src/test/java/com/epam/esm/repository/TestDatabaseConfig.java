package com.epam.esm.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@Profile("test")
@PropertySource("classpath:db-test.properties")
public class TestDatabaseConfig {

    @Value("${db.driver}")
    private String driverName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.pool.min}")
    private String minPoolSizeLine;

    @Value("${db.pool.max}")
    private String maxPoolSizeLine;

    @Bean
    public DataSource mysqlDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(Integer.parseInt(minPoolSizeLine));
        dataSource.setMaxTotal(Integer.parseInt(maxPoolSizeLine));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
