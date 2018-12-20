package com.nicknathanjustin.streamercontracts.reports;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
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
                             @Nullable final ContractModel reportedContract,
                             @NonNull final String report,
                             @NonNull final String contactEmail) {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        final ReportModel reportModel = ReportModel.builder()
                .reportingUser(reportingUser)
                .reportedContract(reportedContract)
                .report(report)
                .contactEmail(contactEmail)
                .createdAt(creationTimestamp)
                .updatedAt(creationTimestamp)
                .build();

        reportModelRepository.save(reportModel);
    }
}
