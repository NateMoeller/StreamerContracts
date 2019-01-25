package com.nicknathanjustin.streamercontracts.alerts;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class AlertMessage {
    private String bountyDescription;
    private BigDecimal bountyAmount;
    private String proposer;
    private AlertType type;
}
