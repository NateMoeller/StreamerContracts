package com.nicknathanjustin.streamercontracts.donations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenDonationDto {
    private UUID donationId;
    private BigDecimal donationAmount;
    private String description;
    private String streamerName;
}
