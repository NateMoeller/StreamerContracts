package com.nicknathanjustin.streamercontracts.twitch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverlayMessage {
    private String bountyDescription;
    private BigDecimal bountyAmount;
    private String proposer;
    private String type;
}
