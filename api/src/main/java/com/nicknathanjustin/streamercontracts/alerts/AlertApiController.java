package com.nicknathanjustin.streamercontracts.alerts;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertApiController {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public boolean testAlert() {
        String username = "test user";
        int amount = 3;
        String bounty = "This is a test bounty";
        AlertMessage testMessage = new AlertMessage(username, bounty, amount);

        messagingTemplate.convertAndSend("/alert/test", testMessage);
        return true;
    }
}
