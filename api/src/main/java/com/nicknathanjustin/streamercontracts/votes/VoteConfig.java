package com.nicknathanjustin.streamercontracts.votes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoteConfig {

    @Autowired
    private VoteModelRepository voteModelRepository;

    @Bean
    public VoteService voteService() {
        return new VoteServiceImpl(voteModelRepository);
    }
}
