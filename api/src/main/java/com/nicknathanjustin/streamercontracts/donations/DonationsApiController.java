package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.donations.requests.CreateDonationRequest;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.security.SecurityService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationsApiController {

    @NonNull private final ContractService contractService;
    @NonNull private final DonationService donationService;
    @NonNull private final PaymentsService paymentsService;
    @NonNull private final SecurityService SecurityService;
    @NonNull private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createDonation(@RequestBody @NonNull final CreateDonationRequest createDonationRequest) {
        final Payment payment = paymentsService.executePayment(createDonationRequest.getPayPalPaymentId()).orElse(null);
        if(payment == null) {
            log.warn("Couldn't execute payment for id: {}", createDonationRequest.getPayPalPaymentId());
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel proposer = userService.getUser(createDonationRequest.getUsername()).orElse(null);
        final UserModel streamer = userService.getUser(createDonationRequest.getStreamerUsername()).orElse(null);
        final String game = createDonationRequest.getGame();
        if (proposer == null || streamer == null) {
            log.warn("Username: {} or StreamerUsername: {} does not exist.", createDonationRequest.getUsername(), createDonationRequest.getStreamerUsername());
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final ContractModel contract = contractService.createContract(proposer, streamer, game, createDonationRequest.getBounty());
        donationService.createDonation(contract, proposer, createDonationRequest.getAmount(), contract.getProposedAt(), createDonationRequest.getPayPalPaymentId());
        return new ResponseEntity(HttpStatus.OK);
    }
}
