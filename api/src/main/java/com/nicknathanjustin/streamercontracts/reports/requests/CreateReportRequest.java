package com.nicknathanjustin.streamercontracts.reports.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.UUID;

@Value
@Builder
public class CreateReportRequest {
    @NonNull private String report;
    @NonNull private String contactEmail;
    @Nullable private UUID reportedContractId;
}
