package com.nicknathanjustin.streamercontracts.emails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Autowired private EmailModelRepository emailModelRepository;

    @Bean
    public EmailService emailService(){ return new EmailServiceImpl(emailModelRepository); }
}
