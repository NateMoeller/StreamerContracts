package com.nicknathanjustin.streamercontracts.alerts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertApiController {

    @NonNull final private SimpMessageSendingOperations messagingTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public boolean testAlert(@NonNull @RequestBody final String alertChannelId) {
        final String title = "New Bounty from User123";
        final String description = "This is a test bounty message";
        final NotificationMessage testMessage = new NotificationMessage(title, description);
        final String url = "/alert/" + alertChannelId;

        messagingTemplate.convertAndSend(url, testMessage);
        return true;
    }
}
