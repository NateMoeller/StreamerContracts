package com.nicknathanjustin.streamercontracts.login;

import com.nicknathanjustin.streamercontracts.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Autowired private UserService userService;

    @Bean
    public LoginService loginService() {
        return new LoginServiceImpl(userService);
    }
}
