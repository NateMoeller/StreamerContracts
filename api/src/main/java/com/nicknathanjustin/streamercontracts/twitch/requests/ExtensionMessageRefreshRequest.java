package com.nicknathanjustin.streamercontracts.twitch.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import lombok.Data;
import lombok.NonNull;

@Data
public class ExtensionMessageRefreshRequest {
    @NonNull private String content_type;
    @NonNull private String message;
    @NonNull private String[] targets;

    public ExtensionMessageRefreshRequest(ContractState contractState) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        this.content_type = "application/json";
        this.message = objectMapper.writeValueAsString(contractState);
        this.targets = new String[]{ "broadcast" };
    }
}
