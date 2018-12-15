package com.nicknathanjustin.streamercontracts.donations.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateDonationRequest {
    @NonNull private String username;
    @NonNull private String payPalPaymentId;
    @NonNull private String bounty;
    @NonNull private BigDecimal amount;
    @NonNull private String streamerUsername;
    @Nullable private String game;
}
