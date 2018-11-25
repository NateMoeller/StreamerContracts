package com.nicknathanjustin.streamercontracts.contracts.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ContractVoteRequest {
    @NonNull private UUID contractId;
    @NonNull private Boolean flagCompleted;
}