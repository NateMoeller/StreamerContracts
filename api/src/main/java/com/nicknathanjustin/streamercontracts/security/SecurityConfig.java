package com.nicknathanjustin.streamercontracts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl();
    }
}
