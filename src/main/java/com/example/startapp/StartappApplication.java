package com.example.startapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StartappApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartappApplication.class, args);

    }

}
