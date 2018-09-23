package com.nicknathanjustin.streamercontracts.donations;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateDonationRequest {
    @NonNull private String username;
    @NonNull private String payPalPaymentId;
    @NonNull private String bounty;
    @NonNull private BigDecimal amount;
}
