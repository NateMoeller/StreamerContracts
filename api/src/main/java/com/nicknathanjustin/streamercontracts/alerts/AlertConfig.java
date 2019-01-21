package com.nicknathanjustin.streamercontracts.alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
public class AlertConfig {

    @Autowired private SimpMessageSendingOperations messagingTemplate;

    @Bean
    public AlertServiceImpl alertService() {
        return new AlertServiceImpl(messagingTemplate);
    }
}
