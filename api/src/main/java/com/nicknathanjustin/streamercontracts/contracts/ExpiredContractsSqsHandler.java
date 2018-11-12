package com.nicknathanjustin.streamercontracts.contracts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class ExpiredContractsSqsHandler {

    @NonNull private final ContractService contractService;

    @SqsListener("ExpiredDonationsSQS-${application.environment}")
    @SuppressWarnings("unused") //Method is invoked when pulling AWS SQS. Method is not directly called within our application.
    public void listen(@NonNull final Object message) {
        final Set<ContractModel> expiredContracts = contractService.getExpiredContracts();
        log.info("expiredContracts count: " + expiredContracts.size());
        expiredContracts.forEach(expiredContract -> {
            //TODO: add service/logic for paying out donations or sending a donation into dispute
        });
    }
}
