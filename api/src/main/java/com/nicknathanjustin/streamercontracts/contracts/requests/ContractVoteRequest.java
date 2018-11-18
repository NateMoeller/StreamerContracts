package com.nicknathanjustin.streamercontracts.contracts.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ContractVoteRequest {
    @NonNull private UUID contractId;
    private boolean flagCompleted;
}
