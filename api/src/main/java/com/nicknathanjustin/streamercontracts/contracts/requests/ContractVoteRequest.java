package com.nicknathanjustin.streamercontracts.contracts.requests;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class ContractVoteRequest {
    @NonNull private UUID contractId;
    @NonNull private Boolean flagCompleted;
}
