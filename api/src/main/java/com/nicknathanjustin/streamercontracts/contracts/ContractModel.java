package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.donations.DonationModel;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class ContractModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    private UserModel proposer;

    @OneToOne
    private UserModel streamer;

    private String game;

    private String description;

    @Setter(AccessLevel.NONE)
    private Timestamp proposedAt;

    @Setter(AccessLevel.NONE)
    private Timestamp activatedAt;
    
    @Setter(AccessLevel.NONE)
    private Timestamp deactivatedAt;

    @Setter(AccessLevel.NONE)
    private Timestamp declinedAt;

    @Setter(AccessLevel.NONE)
    private Timestamp settlesAt;

    @Setter(AccessLevel.NONE)
    private Timestamp expiredAt;

    @Setter(AccessLevel.NONE)
    private Timestamp completedAt;

    @Setter(AccessLevel.NONE)
    private Timestamp failedAt;

    @Setter(AccessLevel.NONE)
    private Timestamp disputedAt;

    private boolean isCommunityContract;

    @Setter(AccessLevel.NONE)
    private ContractState state;

    private String devNote;

    @OneToMany(mappedBy = "contract")
    private List<DonationModel> donations;
    
    @OneToMany(mappedBy = "contract")
    private List<VoteModel> votes;

    public void setContractState(final ContractState newContractState) {
        state = newContractState;
        final Timestamp transitionTimestamp = new Timestamp(System.currentTimeMillis());
        switch (newContractState) {
            case ACTIVE:
                activatedAt = transitionTimestamp;
                break;
            case DECLINED:
                declinedAt = transitionTimestamp;
                break;
            case EXPIRED:
                expiredAt = transitionTimestamp;
                break;
            case COMPLETED:
                completedAt = transitionTimestamp;
                break;
            case FAILED:
                failedAt = transitionTimestamp;
                break;
            case DISPUTED:
                disputedAt = transitionTimestamp;
                break;
            case OPEN:
            	deactivatedAt = transitionTimestamp;
            	break;
            default:
                throw new IllegalStateException(String.format("Error. Attempting to transition a contract to an invalid state. Contract Id: %s State: %s",
                        id,
                        newContractState.name()));
        }
    }
}
