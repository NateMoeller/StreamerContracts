package com.nicknathanjustin.streamercontracts.contracts;

import com.nicknathanjustin.streamercontracts.alerts.AlertService;
import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
import com.nicknathanjustin.streamercontracts.contracts.dtos.PrivateContract;
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

    @NonNull private final AlertService alertService;
    @NonNull private final ContractService contractService;
    @NonNull private final SecurityService securityService;
    @NonNull private final UserService userService;
    @NonNull private final VoteService voteService;

    @RequestMapping(path = "/vote", method = RequestMethod.POST)
    public ResponseEntity voteOnContract(@NonNull final HttpServletRequest httpServletRequest,
                                         @RequestBody @NonNull final ContractVoteRequest contractVoteRequest) {
        if (securityService.isAnonymousRequest(httpServletRequest)) {
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
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable final int page,
            @PathVariable final int pageSize,
            @RequestParam("state") @Nullable final ContractState state,
            @RequestParam("username") @Nullable final String username) {
        final Pageable pageable = PageRequest.of(page, pageSize);

        if (securityService.isAnonymousRequest(httpServletRequest)) {
            return ResponseEntity.ok(contractService.getAllContracts(pageable));
        } else {
            final UserModel user = userService.getUserModelFromRequest(httpServletRequest);
            final UserModel streamer = username != null ? userService.getUser(username).orElse(null) : null;

            if (user != null && streamer != null && state != null) {
                return ResponseEntity.ok(contractService.getContractsForStreamerAndState(
                        streamer,
                        state,
                        pageable,
                        user.getTwitchUsername()));
            } else if (user != null && streamer != null) {
                return ResponseEntity.ok(contractService.getContractsForStreamer(
                        streamer,
                        pageable,
                        user.getTwitchUsername()));
            } else if (state != null) {
                return ResponseEntity.ok(contractService.getContractsForState(state, pageable));
            }
        }

        return ResponseEntity.ok(contractService.getAllContracts(pageable));
    }

    @RequestMapping(path = "donorBounties/{page}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity listContractsForDonator(
            @NonNull final HttpServletRequest httpServletRequest,
            @PathVariable final int page,
            @PathVariable final int pageSize,
            @RequestParam("state") @Nullable final ContractState state) {
        if (securityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel donor = userService.getUserModelFromRequest(httpServletRequest);
        final Pageable pageable = PageRequest.of(page, pageSize);
        Page<Contract> contracts = null;
        if (state != null) {
            contracts = contractService.getContractsForDonorAndState(donor, state, pageable);
        } else {
            contracts = contractService.getContractsForDonor(donor, pageable);
        }

        return ResponseEntity.ok(contracts);
    }

    @RequestMapping(path = "activate", method = RequestMethod.PUT)
    public ResponseEntity activateContract(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NonNull final ContractStateRequest contractStateRequest) {
        if (securityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractStateRequest.getContractId();
        final ContractModel contractModel = contractService.getContract(contractId).orElse(null);
        if (contractModel == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!contractModel.getState().equals(ContractState.OPEN)) {
            throw new IllegalStateException(String.format("Cannot activate a contract that is not OPEN. Contract Id: %s Contract State: %s", contractId, contractModel.getState().name()));
        }

        final UserModel userModel = userService.getUserModelFromRequest(httpServletRequest);
        if (contractModel.getStreamer().getTwitchUsername() != userModel.getTwitchUsername()) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
 
        contractService.activateContract(contractModel);
        final PrivateContract activatedContract = new PrivateContract(contractModel);
        alertService.sendStreamActivateAlert(contractModel.getStreamer(), activatedContract);
        notifyProposer(contractModel,contractModel.getStreamer().getTwitchUsername() + " is attempting your bounty");

        return ResponseEntity.ok(activatedContract);
    }

    @RequestMapping(path = "deactivate", method = RequestMethod.PUT)
    public ResponseEntity deactivateContract(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NonNull final ContractStateRequest contractStateRequest) {
        if (securityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractStateRequest.getContractId();
        final ContractModel contractModel = contractService.getContract(contractId).orElse(null);
        if (contractModel == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!contractModel.getState().equals(ContractState.ACTIVE)) {
            throw new IllegalStateException(String.format("Cannot deactivate a contract that is not ACTIVE. Contract Id: %s Contract STate: %s", contractId, contractModel.getState().name()));
        }

        contractService.deactivateContract(contractModel);
        alertService.sendStreamDeactivateAlert(contractModel.getStreamer(), new PrivateContract(contractModel));

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "decline", method = RequestMethod.PUT)
    public ResponseEntity declineContract(
            @NonNull final HttpServletRequest httpServletRequest,
            @RequestBody @NonNull final ContractStateRequest contractStateRequest) {
        if (securityService.isAnonymousRequest(httpServletRequest)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UUID contractId = contractStateRequest.getContractId();
        final ContractModel contractModel = contractService.getContract(contractId).orElse(null);
        if (contractModel == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!contractModel.getState().equals(ContractState.OPEN) && !contractModel.getState().equals(ContractState.ACTIVE)) {
            throw new IllegalStateException(String.format("Cannot decline a contract that is not OPEN or ACTIVE. Contract Id: %s Contract State: %s", contractId, contractModel.getState().name()));
        }

        contractService.setContractState(contractModel, ContractState.DECLINED);
        contractService.settlePayments(contractModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    private void notifyProposer(@NonNull final ContractModel contractModel, @NonNull final String title) {
        final String description = contractModel.getDescription();
        alertService.sendNotification(contractModel.getProposer(), title, description);
    }
}
