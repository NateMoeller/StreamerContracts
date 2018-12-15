package com.nicknathanjustin.streamercontracts.reports;

import com.nicknathanjustin.streamercontracts.users.UserModel;

public interface ReportService {

    /**
     * Creates a report.
     *
     * @param reportingUser The user making this report. Will be null if report is made by non logged in user.
     * @param report String representing what is being reported.
     * @param contactEmail How to contact the person that made this report.
     */
    void createReport(UserModel reportingUser, String report, String contactEmail);
}
