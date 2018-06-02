package com.nicknathanjustin.streamercontracts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootApiApplication {

    public static void main(String[] args) {
        final ApplicationContext applicationContext = SpringApplication.run(SpringBootApiApplication.class, args);
    }
}