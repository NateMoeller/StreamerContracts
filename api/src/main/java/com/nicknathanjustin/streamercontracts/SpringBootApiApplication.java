package com.nicknathanjustin.streamercontracts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.nicknathanjustin.streamercontracts"})
@Slf4j
public class SpringBootApiApplication {

    public static void main(String[] args) {
        log.info("Starting Spring build at: {}", System.nanoTime());
        SpringApplication.run(SpringBootApiApplication.class, args);
    }
}