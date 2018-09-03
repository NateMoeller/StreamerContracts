package com.nicknathanjustin.streamercontracts.alerts;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AlertSocketController {

    @MessageMapping("/alertQueue")
    @SendTo("/alert/test") // TODO: some id here
    public AlertMessage sendAlert(AlertMessage incomingMsg) throws Exception {
        return incomingMsg;
    }
}
