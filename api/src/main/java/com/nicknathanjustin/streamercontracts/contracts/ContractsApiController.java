package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.requests.ContractVoteRequest;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.nicknathanjustin.streamercontracts.votes.VoteOutcome;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bounties")
@RequiredArgsConstructor
@Slf4j
public class ContractsApiController {

    @NonNull private final ContractService contractService;
    @NonNull private final UserService userService;
    @NonNull private final VoteService voteService;

    @RequestMapping(path = "/vote", method = RequestMethod.POST)
    public ResponseEntity voteOnContract(@RequestBody @NonNull final ContractVoteRequest contractVoteRequest,
                                         @Nullable final OAuth2Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractVoteRequest.getContractId();
        final Optional<ContractModel> optionalContractModel = contractService.getContract(contractId);
        if (!optionalContractModel.isPresent()) {
            log.warn("No contract found for id: {}", contractVoteRequest.getContractId());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        final ContractModel contractModel = optionalContractModel.get();
        final UserModel userModel = userService.getUserFromAuthContext(authentication);

        voteService.recordVote(userModel, contractModel, contractVoteRequest.getFlagCompleted());
        if(voteService.isVotingComplete(contractModel)) {
            final VoteOutcome voteOutcome = voteService.getVoteOutcome(contractModel);
            contractService.settlePayments(contractModel, voteOutcome.isPayStreamer());
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
