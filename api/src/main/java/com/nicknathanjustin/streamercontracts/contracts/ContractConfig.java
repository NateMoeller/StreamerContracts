package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.donations.DonationService;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfig {

    @Autowired private ContractModelRepository contractModelRepository;
    @Autowired private DonationService donationService;
    @Autowired private VoteService voteService;

    @Bean
    public ContractService contractService() {
        return new ContractServiceImpl(contractModelRepository, donationService);
    }

    @Bean
    public ExpiredContractsSqsHandler expiredContractsSqsHandler() {
        return new ExpiredContractsSqsHandler(contractService(), voteService);
    }
}
