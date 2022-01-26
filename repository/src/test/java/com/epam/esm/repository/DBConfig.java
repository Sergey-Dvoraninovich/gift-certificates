package com.epam.esm.repository;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class DBConfig {

    private String driver;
    private String url;
    private String username;
    private String password;
    private String minPoolSize;
    private String maxPoolSize;
}
