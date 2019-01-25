package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.alerts.AlertService;
import com.nicknathanjustin.streamercontracts.contracts.requests.ContractVoteRequest;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContractsApiControllerTest {

    private static final MockHttpServletRequest MOCK_HTTP_SERVLET_REQUEST = new MockHttpServletRequest();
    private static final boolean FLAG_COMPLETED = true;
    private static final UUID CONTRACT_ID = UUID.randomUUID();
    private static final ContractVoteRequest CONTRACT_VOTE_REQUEST = ContractVoteRequest.builder()
            .contractId(CONTRACT_ID)
            .flagCompleted(FLAG_COMPLETED)
            .build();
    private static final UUID PROPOSER_ID = UUID.randomUUID();
    private static final UUID STREAMER_ID = UUID.randomUUID();

    @Mock private SecurityService mockSecurityService;
    @Mock private ContractService mockContractService;
    @Mock private UserService mockUserService;
    @Mock private VoteService mockVoteService;
    @Mock private AlertService mockAlertService;
    @InjectMocks private ContractsApiController contractsApiController;

    @Test(expected = NullPointerException.class)
    public void voteOnContract_nullRequest_throwsException() {
        final ResponseEntity response = contractsApiController.voteOnContract(null, CONTRACT_VOTE_REQUEST);
    }

    @Test(expected = NullPointerException.class)
    public void voteOnContract_nullContractVoteRequest_throwsException() {
        contractsApiController.voteOnContract(MOCK_HTTP_SERVLET_REQUEST, null);
    }

    @Test(expected = NullPointerException.class)
    public void voteOnContract_requestIsUnauthorized_returnsForbidden() {
        final ResponseEntity response = contractsApiController.voteOnContract(MOCK_HTTP_SERVLET_REQUEST, null);

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void voteOnContract_noContractForSuppliedId_returnsNotFound() {
        final ResponseEntity response = contractsApiController.voteOnContract(MOCK_HTTP_SERVLET_REQUEST, CONTRACT_VOTE_REQUEST);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void voteOnContract_validInput_returnsOk() {
        final UserModel userModel = UserModel.builder().build();
        final ContractModel contractModel = ContractModel.builder()
                .proposer(UserModel.builder().id(PROPOSER_ID).build())
                .streamer(UserModel.builder().id(STREAMER_ID).build())
                .build();
        when(mockUserService.getUserModelFromRequest(MOCK_HTTP_SERVLET_REQUEST)).thenReturn(userModel);
        when(mockContractService.getContract(CONTRACT_ID)).thenReturn(Optional.of(contractModel));

        final ResponseEntity response = contractsApiController.voteOnContract(MOCK_HTTP_SERVLET_REQUEST, CONTRACT_VOTE_REQUEST);

        verify(mockVoteService).recordVote(userModel, contractModel, FLAG_COMPLETED);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void voteOnContract_validInputAndVotingIsComplete_setsContractPaymentsAndReturnsOk() {
        final UserModel userModel = UserModel.builder().build();
        final ContractModel contractModel = ContractModel.builder()
                .proposer(UserModel.builder().id(PROPOSER_ID).build())
                .streamer(UserModel.builder().id(STREAMER_ID).build())
                .build();
        final ContractState voteOutcome = ContractState.COMPLETED;
        final VoteModel proposerVote = mockVoteService.getVoteByContractIdAndVoterId(contractModel.getId(), contractModel.getProposer().getId()).orElse(null);
        final VoteModel streamerVote = mockVoteService.getVoteByContractIdAndVoterId(contractModel.getId(), contractModel.getStreamer().getId()).orElse(null);
        when(mockSecurityService.isAnonymousRequest(MOCK_HTTP_SERVLET_REQUEST)).thenReturn(false);
        when(mockUserService.getUserModelFromRequest(MOCK_HTTP_SERVLET_REQUEST)).thenReturn(userModel);
        when(mockContractService.getContract(CONTRACT_ID)).thenReturn(Optional.of(contractModel));
        when(mockVoteService.isVotingComplete(proposerVote, streamerVote, contractModel)).thenReturn(true);
        when(mockVoteService.getVoteOutcome(proposerVote, streamerVote, contractModel)).thenReturn(voteOutcome);

        final ResponseEntity response = contractsApiController.voteOnContract(MOCK_HTTP_SERVLET_REQUEST, CONTRACT_VOTE_REQUEST);

        verify(mockVoteService).recordVote(userModel, contractModel, FLAG_COMPLETED);
        verify(mockContractService).setContractState(contractModel, voteOutcome);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
