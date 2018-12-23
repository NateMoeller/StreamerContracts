package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Autowired private TwitchService twitchService;
    @Autowired private UserModelRepository userModelRepository;

    @Bean
    public UserService userService() {
        return new UserServiceImpl(twitchService, userModelRepository);
    }
}
