package com.nicknathanjustin.streamercontracts.alerts;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationMessage {
    private String title;
    private String description;
}
