package com.project.doctorhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
public class DoctorhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoctorhubApplication.class, args);
    }

}
