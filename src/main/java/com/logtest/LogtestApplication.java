package com.logtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogtestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogtestApplication.class, args);
    }

}
