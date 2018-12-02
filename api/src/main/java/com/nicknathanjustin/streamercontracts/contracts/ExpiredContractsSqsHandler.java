package com.nicknathanjustin.streamercontracts.contracts;

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
    public void settleExpiredDonations(@NonNull final Object message) {
        final Set<ContractModel> expiredContracts = contractService.getExpiredContracts();
        expiredContracts.forEach(expiredContract -> {
            log.info("Settling payments for expiredContract: {}", expiredContract.getId());
            final ContractState voteOutcome = voteService.getVoteOutcome(expiredContract);
            contractService.setContractState(expiredContract, voteOutcome);
        });
    }
}
