package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;

import lombok.Data;
import lombok.NonNull;

@Data
public class PrivateContract extends Contract {

    private boolean userHasVoted;
    
    public PrivateContract(@NonNull final ContractModel contract) {
        super(contract);
        this.userHasVoted = false;
        for (VoteModel vote : contract.getVotes()) {
            if (vote.getVoter().getTwitchUsername().equals(contract.getStreamer().getTwitchUsername())) {
                this.userHasVoted = true;
            }
        }
    }
}
