package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Autowired private TwitchService twitchService;
    @Autowired private UserModelRepository userModelRepository;
    @Autowired private SecurityService securityService;

    @Bean
    public UserService userService() {
        return new UserServiceImpl(twitchService, userModelRepository, securityService);
    }
}
