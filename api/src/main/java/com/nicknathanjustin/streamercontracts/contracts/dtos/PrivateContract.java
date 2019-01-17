package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;

import lombok.Data;
import lombok.NonNull;

import java.util.stream.Stream;

@Data
public class PrivateContract extends Contract {

    private Boolean userVote;
    
    public PrivateContract(@NonNull final ContractModel contract) {
        super(contract);
        this.userVote = null;
        for (VoteModel voteModel : contract.getVotes()) {
            if (voteModel.getVoter().getTwitchUsername().equals(contract.getStreamer().getTwitchUsername())) {
                this.userVote = voteModel.isViewerFlaggedComplete();
            }
        }
    }
}
