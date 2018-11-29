package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.requests.ContractVoteRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.nicknathanjustin.streamercontracts.votes.VoteOutcome;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContractsApiControllerTest {

    private static final OAuth2Authentication AUTHENTICATION = new OAuth2Authentication(getOauth2Request(), getAuthentication());
    private static final boolean FLAG_COMPLETED = true;
    private static final UUID CONTRACT_ID = UUID.randomUUID();
    private static final ContractVoteRequest CONTRACT_VOTE_REQUEST = ContractVoteRequest.builder()
            .contractId(CONTRACT_ID)
            .flagCompleted(FLAG_COMPLETED)
            .build();

    @Mock private ContractService mockContractService;
    @Mock private UserService mockUserService;
    @Mock private VoteService mockVoteService;
    @InjectMocks private ContractsApiController contractsApiController;

    @Test(expected = NullPointerException.class)
    public void voteOnContract_nullContractVoteRequest_throwsException() {
        contractsApiController.voteOnContract(null, AUTHENTICATION);
    }

    @Test
    public void voteOnContract_nullAuthentication_returnsForbidden() {
        final ResponseEntity response = contractsApiController.voteOnContract(CONTRACT_VOTE_REQUEST, null);

        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void voteOnContract_noContractForSuppliedId_returnsNotFound() {
        final ResponseEntity response = contractsApiController.voteOnContract(CONTRACT_VOTE_REQUEST, AUTHENTICATION);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void voteOnContract_validInput_returnsOk() {
        final UserModel userModel = UserModel.builder().build();
        final ContractModel contractModel = ContractModel.builder().build();
        when(mockUserService.getUserFromAuthContext(AUTHENTICATION)).thenReturn(userModel);
        when(mockContractService.getContract(CONTRACT_ID)).thenReturn(Optional.of(contractModel));
        when(mockVoteService.isVotingComplete(contractModel)).thenReturn(false);

        final ResponseEntity response = contractsApiController.voteOnContract(CONTRACT_VOTE_REQUEST, AUTHENTICATION);

        verify(mockVoteService).recordVote(userModel, contractModel, FLAG_COMPLETED);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void voteOnContract_validInputAndVotingIsComplete_settlesContractPaymentsAndReturnsOk() {
        final UserModel userModel = UserModel.builder().build();
        final ContractModel contractModel = ContractModel.builder().build();
        final VoteOutcome voteOutcome = VoteOutcome.COMPLETED;
        when(mockUserService.getUserFromAuthContext(AUTHENTICATION)).thenReturn(userModel);
        when(mockContractService.getContract(CONTRACT_ID)).thenReturn(Optional.of(contractModel));
        when(mockVoteService.isVotingComplete(contractModel)).thenReturn(true);
        when(mockVoteService.getVoteOutcome(contractModel)).thenReturn(voteOutcome);

        final ResponseEntity response = contractsApiController.voteOnContract(CONTRACT_VOTE_REQUEST, AUTHENTICATION);

        verify(mockVoteService).recordVote(userModel, contractModel, FLAG_COMPLETED);
        verify(mockContractService).settlePayments(contractModel, voteOutcome.isPayStreamer());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private static OAuth2Request getOauth2Request () {
        final String clientId = "oauth-client-id";
        final Map<String, String> requestParameters = Collections.emptyMap();
        final boolean approved = true;
        final String redirectUrl = "http://my-redirect-url.com";
        final Set<String> responseTypes = Collections.emptySet();
        final Set<String> scopes = Collections.emptySet();
        final Set<String> resourceIds = Collections.emptySet();
        final Map<String, Serializable> extensionProperties = Collections.emptyMap();
        final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");

        return new OAuth2Request(requestParameters, clientId, authorities,
                approved, scopes, resourceIds, redirectUrl, responseTypes, extensionProperties);
    }

    private static Authentication getAuthentication() {
        final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("Everything");
        final User userPrincipal = new User("user", "", true, true, true, true, authorities);
        final TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);

        return token;
    }
}
