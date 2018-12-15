package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.donations.DonationModel;
import com.nicknathanjustin.streamercontracts.donations.OpenDonationDto;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ContractDto {
    private UUID contractId;
    private BigDecimal contractAmount;
    private String description;
    private boolean isCommunity;
    private String game;
    private Timestamp proposedAt;
    private Timestamp acceptedAt;
    private Timestamp declinedAt;
    private Timestamp settlesAt;
    private Timestamp expiredAt;
    private Timestamp completedAt;
    private Timestamp failedAt;
    private Timestamp disputedAt;
    private String streamerName;
    private String bountyOwnerName;
    private List<OpenDonationDto> donations;

    public ContractDto(
            @NonNull final ContractModel contract,
            @NonNull final BigDecimal contractTotal) {
        contractId = contract.getId();
        description = contract.getDescription();
        isCommunity = contract.isCommunityContract();
        game = contract.getGame();
        proposedAt = contract.getProposedAt();
        acceptedAt = contract.getAcceptedAt();
        declinedAt = contract.getDeclinedAt();
        settlesAt = contract.getSettlesAt();
        expiredAt = contract.getExpiredAt();
        completedAt = contract.getCompletedAt();
        failedAt = contract.getFailedAt();
        disputedAt = contract.getDisputedAt();
        streamerName = contract.getStreamer().getTwitchUsername();
        bountyOwnerName = contract.getProposer().getTwitchUsername();
        contractAmount = contractTotal;
        donations = new ArrayList<OpenDonationDto>();
        // TODO: Remove the redundancy here
        for (DonationModel donation : contract.getDonations()) {
            donations.add(new OpenDonationDto(
                    donation.getId(),
                    donation.getDonationAmount(),
                    contract.getDescription(),
                    contract.getStreamer().getTwitchUsername()));
        }
    }
}
