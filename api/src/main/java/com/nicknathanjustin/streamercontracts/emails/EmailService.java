package com.nicknathanjustin.streamercontracts.emails;

public interface EmailService {
    /**
     * Creates the email in the database.
     *
     * @param  name The name of the user.
     * @param  email The email of the user
     */
    void createEmail(String name, String email);
}
