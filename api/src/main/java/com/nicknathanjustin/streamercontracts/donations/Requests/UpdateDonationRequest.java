package com.nicknathanjustin.streamercontracts.donations.Requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateDonationRequest {
    @NonNull private UUID donationId;
    private boolean markedCompleted;
}
