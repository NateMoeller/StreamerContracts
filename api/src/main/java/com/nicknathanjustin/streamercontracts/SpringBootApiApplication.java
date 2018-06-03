package com.nicknathanjustin.streamercontracts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
public class SpringBootApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApiApplication.class, args);
    }
}