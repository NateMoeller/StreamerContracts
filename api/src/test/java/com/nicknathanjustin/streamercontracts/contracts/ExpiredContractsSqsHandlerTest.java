package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpiredContractsSqsHandlerTest {

    @Mock private VoteService mockVoteService;
    @Mock private ContractService mockContractService;
    @InjectMocks private ExpiredContractsSqsHandler expiredContractsSqsHandler;

    @Test(expected = NullPointerException.class)
    public void settleContracts_nullInput_throwsException() {
        expiredContractsSqsHandler.settleAndExpireContracts(null);
    }

    @Test
    public void settleContracts_noSettleableContracts_settlesNoContracts() {
        expiredContractsSqsHandler.settleAndExpireContracts(new Object());

        verify(mockContractService, never()).setContractState(any(), any());
    }

    @Test
    public void settleContracts_settleableContracts_settlesContracts() {
        final Set<ContractModel> settleableContracts = new HashSet<>();
        final int numberOfSettleableContracts = 15;
        for (int i = 0; i < numberOfSettleableContracts; i++) {
            settleableContracts.add(ContractModel.builder().build());
        }
        when(mockContractService.getSettleableContracts()).thenReturn(settleableContracts);
        final ContractState voteOutcome = ContractState.COMPLETED;
        when(mockVoteService.getVoteOutcome(any())).thenReturn(voteOutcome);

        expiredContractsSqsHandler.settleAndExpireContracts(new Object());

        verify(mockContractService, times(numberOfSettleableContracts)).setContractState(any(), eq(voteOutcome));
    }
}
