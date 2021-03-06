package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.epam.esm"})
@EnableSwagger2
//@OpenAPIDefinition
public class GiftCertificatesApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GiftCertificatesApplication.class);
    }
}
