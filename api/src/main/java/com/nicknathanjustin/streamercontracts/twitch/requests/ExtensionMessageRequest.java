package com.nicknathanjustin.streamercontracts.twitch.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;

@Data
public class ExtensionMessageRequest {
    @NonNull private String content_type;
    @NonNull private String message;
    @NonNull private String[] targets;

    public ExtensionMessageRequest(@NonNull Object message) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        this.content_type = "application/json";
        this.message = objectMapper.writeValueAsString(message);
        this.targets = new String[]{ "broadcast" };
    }
}
