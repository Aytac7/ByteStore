package com.example.startapp;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.startapp.service.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StartappApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartappApplication.class, args);

    }

}
