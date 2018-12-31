package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.contracts.dtos.ContractDto;
import com.nicknathanjustin.streamercontracts.contracts.requests.ContractStateRequest;
import com.nicknathanjustin.streamercontracts.contracts.requests.ContractVoteRequest;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.nicknathanjustin.streamercontracts.votes.VoteModel;
import com.nicknathanjustin.streamercontracts.votes.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bounties")
@RequiredArgsConstructor
@Slf4j
public class ContractsApiController {

    @NonNull private final ContractService contractService;
    @NonNull private final SecurityService SecurityService;
    @NonNull private final UserService userService;
    @NonNull private final VoteService voteService;

    @RequestMapping(path = "/vote", method = RequestMethod.POST)
    public ResponseEntity voteOnContract(@NonNull final HttpServletRequest httpServletRequest,
                                         @RequestBody @NonNull final ContractVoteRequest contractVoteRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractVoteRequest.getContractId();
        final Optional<ContractModel> optionalContractModel = contractService.getContract(contractId);
        if (!optionalContractModel.isPresent()) {
            log.warn("No contract found for id: {}", contractVoteRequest.getContractId());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        final ContractModel contractModel = optionalContractModel.get();
        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);

        // TODO: Need to catch the exception here if voting on the contract fails
        voteService.recordVote(userModel, contractModel, contractVoteRequest.getFlagCompleted());
        final VoteModel proposerVote = voteService.getVoteByContractIdAndVoterId(contractId, contractModel.getProposer().getId()).orElse(null);
        final VoteModel streamerVote = voteService.getVoteByContractIdAndVoterId(contractId, contractModel.getStreamer().getId()).orElse(null);

        // TODO: Propagate back to front end that voting on a contract you've already voted on is not allowed
        if(voteService.isVotingComplete(proposerVote, streamerVote, contractModel)) {
            final ContractState voteOutcome = voteService.getVoteOutcome(proposerVote, streamerVote, contractModel);
            contractService.setContractState(contractModel, voteOutcome);
            contractService.settlePayments(contractModel);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "streamerBounties/{page}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity listContractsForStreamer(
            @PathVariable final int page,
            @PathVariable final int pageSize,
            @RequestParam("state") @Nullable final ContractState state,
            @RequestParam("username") @Nullable final String username) {
        final Pageable pageable = PageRequest.of(page, pageSize);
        Page<ContractDto> contracts = null;
        final UserModel streamer = userService.getUser(username).orElse(null);
        if (streamer != null && state != null) {
            contracts = contractService.getContractsForStreamerAndState(streamer, state, pageable);
        } else if (streamer != null) {
            contracts = contractService.getContractsForStreamer(streamer, pageable);
        } else if (state != null) {
            contracts = contractService.getContractsForState(state, pageable);
        } else {
            contracts = contractService.getAllContracts(pageable);
        }

        return ResponseEntity.ok(contracts);
    }

    @RequestMapping(path = "donorBounties/{page}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity listContractsForDonator(
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable final int page,
            @PathVariable final int pageSize,
            @RequestParam("state") @Nullable final ContractState state) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel donor = userService.getUserModelFromRequest(httpServletRequest);
        final Pageable pageable = PageRequest.of(page, pageSize);
        Page<ContractDto> contracts = null;
        if (state != null) {
            contracts = contractService.getContractsForDonatorAndState(donor, state, pageable);
        } else {
            contracts = contractService.getContractsForDonator(donor, pageable);
        }

        return ResponseEntity.ok(contracts);
    }

    @RequestMapping(path = "accept", method = RequestMethod.PUT)
    public ResponseEntity acceptContract(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NonNull final ContractStateRequest contractStateRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractStateRequest.getContractId();
        final ContractModel contractModel = contractService.getContract(contractId).orElse(null);
        if (contractModel == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!contractModel.getState().equals(ContractState.OPEN)) {
            throw new IllegalStateException(String.format("Cannot accept a contract that is not OPEN. Contract Id: %s Contract State: %s", contractId, contractModel.getState().name()));
        }

        contractService.setContractState(contractModel, ContractState.ACCEPTED);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "decline", method = RequestMethod.PUT)
    public ResponseEntity declineContract(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NonNull final ContractStateRequest contractStateRequest) {
        if (SecurityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractStateRequest.getContractId();
        final ContractModel contractModel = contractService.getContract(contractId).orElse(null);
        if (contractModel == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!contractModel.getState().equals(ContractState.OPEN)) {
            throw new IllegalStateException(String.format("Cannot decline a contract that is not OPEN. Contract Id: %s Contract State: %s", contractId, contractModel.getState().name()));
        }

        contractService.setContractState(contractModel, ContractState.DECLINED);
        contractService.settlePayments(contractModel);
        return new ResponseEntity(HttpStatus.OK);
    }
}
