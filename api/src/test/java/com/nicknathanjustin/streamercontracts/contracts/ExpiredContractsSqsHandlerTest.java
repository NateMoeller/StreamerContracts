package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.votes.VoteOutcome;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
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
    public void settleExpiredDonations_nullInput_throwsException() {
        expiredContractsSqsHandler.settleExpiredDonations(null);
    }

    @Test
    public void settleExpiredDonations_noExpiredDonations_settlesNoContracts() {
        expiredContractsSqsHandler.settleExpiredDonations(new Object());

        verify(mockContractService, never()).settlePayments(any(), anyBoolean());
    }

    @Test
    public void settleExpiredDonations_expiredDonations_settlesExpiredContracts() {
        final Set<ContractModel> expiredContracts = new HashSet<>();
        final int numberOfExpiredContracts = 15;
        for (int i=0; i<numberOfExpiredContracts; i++) {
            expiredContracts.add(ContractModel.builder().build());
        }
        when(mockContractService.getExpiredContracts()).thenReturn(expiredContracts);
        final VoteOutcome voteOutcome = VoteOutcome.COMPLETED;
        when(mockVoteService.getVoteOutcome(any())).thenReturn(voteOutcome);

        expiredContractsSqsHandler.settleExpiredDonations(new Object());

        verify(mockContractService, times(numberOfExpiredContracts)).settlePayments(any(), eq(voteOutcome.isPayStreamer()));
    }
}
