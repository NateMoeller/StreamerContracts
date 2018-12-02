package com.nicknathanjustin.streamercontracts.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSettingsConfig {

    @Autowired
    private UserSettingsModelRepository userSettingsModelRepository;

    @Bean
    public UserSettingsService userSettingsService() {
        return new UserSettingsServiceImpl(userSettingsModelRepository);
    }
}
