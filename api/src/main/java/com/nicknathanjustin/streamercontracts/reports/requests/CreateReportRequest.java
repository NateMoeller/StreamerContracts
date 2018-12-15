package com.nicknathanjustin.streamercontracts.reports.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CreateReportRequest {
    @NonNull private String report;
    @NonNull private String contactEmail;
}
