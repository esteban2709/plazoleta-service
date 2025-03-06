package com.pragma.plazoletaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableMethodSecurity(prePostEnabled = true)
public class PlazoletaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlazoletaServiceApplication.class, args);
    }

}
