package com.nicknathanjustin.streamercontracts.alerts;

import lombok.Data;
import lombok.NonNull;

@Data
public class AlertMessage {
    private String username;
    private String bounty;
    private int amount;

    public AlertMessage() {

    }

    public AlertMessage(@NonNull final String username, @NonNull final String bounty, final int amount) {
        this.username = username;
        this.bounty = bounty;
        this.amount = amount;
    }
}
