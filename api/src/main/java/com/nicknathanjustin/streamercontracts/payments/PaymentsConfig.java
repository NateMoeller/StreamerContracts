package com.nicknathanjustin.streamercontracts.payments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentsConfig {

    @Bean
    public PaymentsService paymentsService() {
        return new PaymentsServiceImpl();
    }
}
