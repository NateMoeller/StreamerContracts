package com.nicknathanjustin.streamercontracts.twitch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitchConfig {

    @Bean
    public TwitchService twitchService() { return new TwitchServiceImpl(); }
}
