package com.nicknathanjustin.streamercontracts.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfig {

    @Autowired
    private ContractModelRepository contractModelRepository;

    @Bean
    public ContractService contractService() {
        return new ContractServiceImpl(contractModelRepository);
    }

    @Bean
    public ExpiredContractsSqsHandler expiredContractsSqsHandler() {
        return new ExpiredContractsSqsHandler(contractService());
    }
}
