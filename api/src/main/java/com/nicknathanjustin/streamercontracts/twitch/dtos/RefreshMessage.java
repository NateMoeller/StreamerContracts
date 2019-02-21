package com.nicknathanjustin.streamercontracts.twitch.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshMessage {
    private ContractState contractState;
}
