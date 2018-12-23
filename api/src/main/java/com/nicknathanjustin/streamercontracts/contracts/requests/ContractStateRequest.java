package com.nicknathanjustin.streamercontracts.contracts.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ContractStateRequest {
    @NonNull private UUID contractId;
}
