package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.alerts.AlertService;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DonationConfig {

    @Autowired private AlertService alertService;
    @Autowired private DonationModelRepository donationModelRepository;
    @Autowired private PaymentsService paymentsService;
    @Autowired private TwitchService twitchService;

    @Bean
    public DonationService donationService() {
        return new DonationServiceImpl(alertService, donationModelRepository, paymentsService);
    }
}
