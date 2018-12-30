package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DonationConfig {

    @Autowired private DonationModelRepository donationModelRepository;
    @Autowired private PaymentsService paymentsService;
    @Autowired private SecurityService SecurityService;

    @Bean
    public DonationService donationService() {
        return new DonationServiceImpl(donationModelRepository, paymentsService);
    }
}
