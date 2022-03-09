package com.example.task3_1_4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class Task314Application {

    public static void main(String[] args) {
        SpringApplication.run(Task314Application.class, args);
    }

}
