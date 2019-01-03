package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
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
    private ContractState state;
    private BigDecimal contractAmount;
    private String description;
    private boolean isCommunity;
    private String game;
    private Timestamp proposedAt;
    private Timestamp activatedAt;
    private Timestamp deactivatedAt;
    private Timestamp declinedAt;
    private Timestamp settlesAt;
    private Timestamp expiredAt;
    private Timestamp completedAt;
    private Timestamp failedAt;
    private Timestamp disputedAt;
    private String streamerName;
    private String proposerName;
    private List<DonationDto> donations;

    public ContractDto(@NonNull final ContractModel contract) {
        contractId = contract.getId();
        state = contract.getState();
        description = contract.getDescription();
        isCommunity = contract.isCommunityContract();
        game = contract.getGame();
        proposedAt = contract.getProposedAt();
        activatedAt = contract.getActivatedAt();
        deactivatedAt = contract.getDeactivatedAt();
        declinedAt = contract.getDeclinedAt();
        settlesAt = contract.getSettlesAt();
        expiredAt = contract.getExpiredAt();
        completedAt = contract.getCompletedAt();
        failedAt = contract.getFailedAt();
        disputedAt = contract.getDisputedAt();
        streamerName = contract.getStreamer().getTwitchUsername();
        proposerName = contract.getProposer().getTwitchUsername();
        contractAmount = new BigDecimal(0);
        donations = new ArrayList<>();
        for (DonationModel donation : contract.getDonations()) {
            // We're re-computing a SUM when we could be just issuing a subquery
            // This is because it is difficult with Spring Pagination to issue a subquery.
            // Since we already need to loop over the donations, we just sum up the donation total here.
            contractAmount = contractAmount.add(donation.getDonationAmount());
            donations.add(new DonationDto(
                    donation.getDonationAmount(),
                    contract.getProposer().getTwitchUsername(),
                    donation.getCreatedAt()));
        }
    }
}
