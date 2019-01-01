package com.nicknathanjustin.streamercontracts.donations.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationDto {
    private BigDecimal donationAmount;
    private String donorUsername;
    private Timestamp createdAt;
}
