package com.nicknathanjustin.streamercontracts.alerts;


import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
import com.nicknathanjustin.streamercontracts.users.UserModel;

public interface AlertService {
    /**
     * Sends a notification to the frontend.
     *
     * @param  user the user to send the notification to
     * @param  title The title of the notification.
     * @param  description The description of the notification.
     */
    void sendNotification(UserModel user, String title, String description);

    /**
     * Sends an activate alert to the frontend
     *
     * @param  user the user to send the alert
     * @param  contract The contract to activate.
     */
    void sendStreamActivateAlert(UserModel user, Contract contract);

    /**
     * Sends a deactivate alert to the frontend
     *
     * @param  user the user to send the alert
     * @param  contract The contract to deactivate.
     */
    void sendStreamDeactivateAlert(UserModel user, Contract contract);
}
