package com.nicknathanjustin.streamercontracts.alerts;

public interface AlertService {
    /**
     * Sends an alert to the frontend.
     *
     * @param  alertChannelId the websocket channel id to send the alert
     * @param  title The title of the alert.
     * @param  description The description of the alert.
     */
    void sendAlert(String alertChannelId, String title, String description);
}
