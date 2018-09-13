package com.nicknathanjustin.streamercontracts.alerts;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AlertMessage {
    private String username;
    private String bounty;
    private BigDecimal amount;

    public AlertMessage(@NonNull final String username, @NonNull final String bounty, final BigDecimal amount) {
        this.username = username;
        this.bounty = bounty;
        this.amount = amount;
    }
}
