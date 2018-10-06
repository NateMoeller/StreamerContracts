package com.nicknathanjustin.streamercontracts.donations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenDonationDto {
    private UUID donationId;
    private BigDecimal donationAmount;
    private String description;
    private String streamerName;

    static OpenDonationDto fromEntity(@NonNull final DonationModel donationModel) {
        return OpenDonationDto.builder()
                .donationId(donationModel.getId())
                .donationAmount(donationModel.getDonationAmount())
                .description(donationModel.getContract().getDescription())
                .streamerName(donationModel.getContract().getStreamer().getTwitchUsername())
                .build();
    }
}
