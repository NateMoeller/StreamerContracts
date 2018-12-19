package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpiredContractsSqsHandlerTest {

    private static final UUID PROPOSER_ID = UUID.randomUUID();
    private static final UUID STREAMER_ID = UUID.randomUUID();

    @Mock private VoteService mockVoteService;
    @Mock private ContractService mockContractService;
    @InjectMocks private ExpiredContractsSqsHandler expiredContractsSqsHandler;

    @Test(expected = NullPointerException.class)
    public void settleAndExpireContracts_nullInput_throwsException() {
        expiredContractsSqsHandler.settleAndExpireContracts(null);
    }

    @Test
    public void settleAndExpireContracts_noSettleableContracts_settlesNoContracts() {
        expiredContractsSqsHandler.settleAndExpireContracts(new Object());

        verify(mockContractService, never()).setContractState(any(), any());
    }

    @Test
    public void settleAndExpireContracts_settleableContracts_settlesContracts() {
        final UserModel proposer = UserModel.builder().id(PROPOSER_ID).build();
        final UserModel streamer = UserModel.builder().id(STREAMER_ID).build();
        final Set<ContractModel> settleableContracts = new HashSet<>();
        final int numberOfSettleableContracts = 15;
        for (int i = 0; i < numberOfSettleableContracts; i++) {
            ContractModel contractModel = ContractModel.builder().streamer(streamer).proposer(proposer).id(UUID.randomUUID()).build();
            VoteModel proposerVote = VoteModel.builder().voter(proposer).contract(contractModel).build();
            VoteModel streamerVote = VoteModel.builder().voter(streamer).contract(contractModel).build();
            when(mockVoteService.getVoteByContractIdAndVoterId(contractModel.getId(), PROPOSER_ID)).thenReturn(Optional.of(proposerVote));
            when(mockVoteService.getVoteByContractIdAndVoterId(contractModel.getId(), STREAMER_ID)).thenReturn(Optional.of(streamerVote));
            settleableContracts.add(contractModel);
        }
        when(mockContractService.getSettleableContracts()).thenReturn(settleableContracts);
        final ContractState contractState = ContractState.COMPLETED;
        when(mockVoteService.getVoteOutcome(any(), any(), any())).thenReturn(contractState);

        expiredContractsSqsHandler.settleAndExpireContracts(new Object());

        verify(mockContractService, times(numberOfSettleableContracts)).setContractState(any(), eq(contractState));
    }
}
