package com.nicknathanjustin.streamercontracts.donations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DonationConfig {

    @Autowired
    private DonationModelRepository donationModelRepository;

    @Bean
    public DonationService donationService() {
        return new DonationServiceImpl(donationModelRepository);
    }
}
