package com.nicknathanjustin.streamercontracts.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfig {

    @Autowired
    private ReportModelRepository reportModelRepository;

    @Bean
    public ReportService reportService() {
        return new ReportServiceImpl(reportModelRepository);
    }
}
