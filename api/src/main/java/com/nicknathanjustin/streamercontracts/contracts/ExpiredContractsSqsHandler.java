package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.votes.VoteModel;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class ExpiredContractsSqsHandler {

    @NonNull private final ContractService contractService;
    @NonNull private final VoteService voteService;

    @SqsListener("ExpiredDonationsSQS-${application.environment}")
    @SuppressWarnings("unused") // Method is invoked when pulling AWS SQS. Method is not directly called within our application.
    public void settleAndExpireContracts(@NonNull final Object message) {
        final Set<ContractModel> settleableContracts = contractService.getSettleableContracts();
        settleableContracts.forEach(contract -> {
            final VoteModel proposerVote = voteService.getVoteByContractIdAndVoterId(contract.getId(), contract.getProposer().getId()).orElse(null);
            final VoteModel streamerVote = voteService.getVoteByContractIdAndVoterId(contract.getId(), contract.getStreamer().getId()).orElse(null);
            log.info("Settling payments for contract: {}", contract.getId());
            final ContractState voteOutcome = voteService.getVoteOutcome(proposerVote, streamerVote, contract);
            contractService.setContractState(contract, voteOutcome);
        });
    }
}
