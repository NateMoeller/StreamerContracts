package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationsApiController {

    @NonNull private final PaymentsService paymentsService;

    @NonNull private final ContractService contractService;

    @NonNull private final UserService userService;

    @NonNull private final DonationService donationService;

    @RequestMapping(path = "/execute", method = RequestMethod.POST)
    public ResponseEntity executePayment(@RequestBody @NonNull final CreateDonationRequest createDonationRequest) {
        final Payment payment = paymentsService.executePayment(createDonationRequest.getPayPalPaymentId()).orElse(null);
        if(payment == null) {
            log.warn("Couldn't execute payment for id: " + createDonationRequest.getPayPalPaymentId());
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final Optional<UserModel> proposerObj = userService.getUser(createDonationRequest.getUsername());
        final Optional<UserModel> streamerObj = userService.getUser(createDonationRequest.getStreamerUsername());
        if (!proposerObj.isPresent() || !streamerObj.isPresent()) {
            log.warn("Username: " + createDonationRequest.getUsername() + " or StreamerUsername: " + createDonationRequest.getStreamerUsername() + " does not exist.");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel proposer = proposerObj.get();
        final UserModel streamer = streamerObj.get();
        final ContractModel contract = contractService.createContract(proposer.getId(), streamer.getId(), null, createDonationRequest.getBounty());
        donationService.createDonation(contract.getId(), proposer.getId(), createDonationRequest.getAmount(), contract.getProposedAt(), createDonationRequest.getPayPalPaymentId());
        return ResponseEntity.ok(payment.toJSON());
    }

    //TODO: turn these all into POST requests
    @RequestMapping(path = "/capture/{authorizationId}", method = RequestMethod.GET)
    public boolean capturePayment(@PathVariable("authorizationId") @NonNull final String authorizationId) {
        return paymentsService.capturePayment(authorizationId);
    }

    @RequestMapping(path = "/void/{authorizationId}", method = RequestMethod.GET)
    public boolean voidPayment(@PathVariable("authorizationId") @NonNull final String authorizationId) {
        return paymentsService.voidPayment(authorizationId);
    }
}
