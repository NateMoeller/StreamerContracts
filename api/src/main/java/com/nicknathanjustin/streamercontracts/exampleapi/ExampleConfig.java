package com.nicknathanjustin.streamercontracts.exampleapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleConfig {

    @Autowired
    private ExampleModelRepository exampleModelRepository;

    @Bean
    public ExampleService exampleService() {
        return new ExampleServiceImpl(exampleModelRepository);
    }
}
