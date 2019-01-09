package com.nicknathanjustin.streamercontracts.security;

import com.nicknathanjustin.streamercontracts.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Autowired private UserService userService;

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl(userService);
    }
}
