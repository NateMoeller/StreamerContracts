package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.donations.DonationModel;
import com.nicknathanjustin.streamercontracts.donations.dtos.DonationDto;
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
    private List<DonationDto> donations;

    public ContractDto(
            @NonNull final ContractModel contract) {
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
        contractAmount = new BigDecimal(0);
        donations = new ArrayList<DonationDto>();
        for (DonationModel donation : contract.getDonations()) {
            // TODO: This is so ugly and I hate it. We're re-computing a SUM when we could be just issuing a subquery
            // We're doing it this way because I cant figure out how to issue a subquery in Hibernate and use
            // Spring's pagination. Ugly, ugly, ugly.
            contractAmount = contractAmount.add(donation.getDonationAmount());
            donations.add(new DonationDto(
                    donation.getDonationAmount(),
                    contract.getProposer().getTwitchUsername(),
                    donation.getDonatedAt()));
        }
    }
}
