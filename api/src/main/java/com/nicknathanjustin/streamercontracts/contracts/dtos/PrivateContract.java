package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;

import lombok.Data;
import lombok.NonNull;

import java.util.stream.Stream;

@Data
public class PrivateContract extends Contract {

    private String userVote;
    
    public PrivateContract(@NonNull final ContractModel contract, final String donorUsername) {
        super(contract);
        this.userVote = null;
        this.setUserVote(contract, donorUsername);
    }

    public PrivateContract(@NonNull final ContractModel contract) {
        super(contract);
        this.userVote = null;
        this.setUserVote(contract, contract.getStreamer().getTwitchUsername());
    }

    private void setUserVote(ContractModel contract, String username) {
        for (VoteModel voteModel : contract.getVotes()) {
            if (voteModel.getVoter().getTwitchUsername().equals(username)) {
                this.userVote = voteModel.isViewerFlaggedComplete() ? "COMPLETED" : "FAILED";
            }
        }
    }
}
