package com.nicknathanjustin.streamercontracts.contracts.dtos;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;

import lombok.Data;
import lombok.NonNull;

import java.util.stream.Stream;

@Data
public class PrivateContract extends Contract {

    private boolean userHasVoted;
    
    public PrivateContract(@NonNull final ContractModel contract) {
        super(contract);
        Stream<VoteModel> userVotes = contract.getVotes().stream().filter(voteModel -> voteModel.getVoter().getTwitchUsername().equals(contract.getStreamer().getTwitchUsername()));
        this.userHasVoted = userVotes.count() > 0;
    }
}
