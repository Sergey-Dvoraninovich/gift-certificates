package com.epam.esm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
//@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = "com.epam.esm.repository")
@ComponentScan("com.epam.esm.*")
@EntityScan("com.epam.esm.*")
@EnableTransactionManagement
@EnableAutoConfiguration
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class TestDatabaseConfig {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.minPoolSize}")
    private String minPoolSize;

    @Value("${db.minPoolSize}")
    private String maxPoolSize;

    @Autowired
    JpaVendorAdapter jpaVendorAdapter;

    @Bean
    public DataSource mysqlDataSource() {
//        BasicDataSource dataSource = new BasicDataSource();

//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setMinIdle(Integer.parseInt(minPoolSize));
//        dataSource.setMaxTotal(Integer.parseInt(maxPoolSize));

            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setName("in_memory_certificates")
                    .addScript("classpath:init_schema.sql")
                    .addScript("classpath:init_data.sql")
                    .build();

        //return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(mysqlDataSource());
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("com.epam.esm.entity");
        emf.setPersistenceUnitName("default");
        emf.afterPropertiesSet();
        return emf.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory());
        return tm;
    }
}
