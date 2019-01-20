package com.nicknathanjustin.streamercontracts.alerts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    @NonNull final private SimpMessageSendingOperations messagingTemplate;

    @Override
    public void sendAlert(@NonNull final String alertChannelId, @NonNull final String title, @NonNull final String description) {
        final AlertMessage message = new AlertMessage(title, description);
        final String url = "/alert/" + alertChannelId;

        messagingTemplate.convertAndSend(url, message);
    }
}
