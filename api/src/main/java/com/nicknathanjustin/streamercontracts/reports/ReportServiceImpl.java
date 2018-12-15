package com.nicknathanjustin.streamercontracts.reports;

import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;

@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    @NonNull final ReportModelRepository reportModelRepository;

    @Override
    public void createReport(@Nullable final UserModel reportingUser,
                             @NonNull final String report,
                             @NonNull final String contactEmail) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        final ReportModel reportModel = ReportModel.builder()
                .reportingUser(reportingUser)
                .report(report)
                .contactEmail(contactEmail)
                .createdAt(creationTimestamp)
                .updatedAt(creationTimestamp)
                .build();

        reportModelRepository.save(reportModel);
    }
}
