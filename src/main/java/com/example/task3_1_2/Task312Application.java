package com.example.task3_1_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@SpringBootApplication
public class Task312Application {

    public static void main(String[] args) {
        SpringApplication.run(Task312Application.class, args);
    }

}
