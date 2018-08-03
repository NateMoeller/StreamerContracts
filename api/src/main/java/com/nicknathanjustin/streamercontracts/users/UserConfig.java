package com.nicknathanjustin.streamercontracts.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Autowired
    private UserModelRepository userModelRepository;

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userModelRepository);
    }
}
