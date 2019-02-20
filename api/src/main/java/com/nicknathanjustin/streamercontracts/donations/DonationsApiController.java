package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.alerts.AlertService;
import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.donations.requests.CreateDonationRequest;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationsApiController {

    @NonNull private final AlertService alertService;
    @NonNull private final ContractService contractService;
    @NonNull private final DonationService donationService;
    @NonNull private final PaymentsService paymentsService;
    @NonNull private final TwitchService twitchService;
    @NonNull private final UserService userService;

    @Value("${application.blackListedWords}")
    private String[] blackListedWords;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> createDonation(@RequestBody @NonNull final CreateDonationRequest createDonationRequest) {
        final UserModel proposer = userService.getUser(createDonationRequest.getUsername()).orElse(null);
        final UserModel streamer = userService.getUser(createDonationRequest.getStreamerUsername()).orElse(null);
        if (proposer == null || streamer == null) {
            log.warn("Username: {} or StreamerUsername: {} does not exist.", createDonationRequest.getUsername(), createDonationRequest.getStreamerUsername());
            return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
        }

        final String paypalPaymentId = createDonationRequest.getPayPalPaymentId();
        final Payment payment = paymentsService.executePayment(paypalPaymentId).orElse(null);
        if(payment == null) {
            log.warn("Couldn't execute payment for id: {}", paypalPaymentId);
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final String authorizationId = getAuthorizationId(payment);
        if(authorizationId == null) {
            log.warn("Payment executed, but couldn't extract authorizationId for paymentId: {}", paypalPaymentId);
            return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final ContractModel contract = contractService.createContract(proposer, streamer, createDonationRequest.getGame(), createDonationRequest.getBounty());
        checkForBlackListedWords(createDonationRequest.getBounty(), contract.getId());
        donationService.createDonation(contract, proposer, createDonationRequest.getAmount(), contract.getProposedAt(), paypalPaymentId, authorizationId);

        final String title = "New bounty from " + contract.getProposer().getTwitchUsername();
        alertService.sendNotification(streamer, title, contract.getDescription());

        twitchService.sendExtensionRefresh(contract.getState(), streamer.getTwitchId());

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);

    }

    private String getAuthorizationId(@NonNull final Payment payment) {
        final int numberTransactions = payment.getTransactions().size();
        if (numberTransactions != 1) {
            log.error("payment had {} number of transactions. Unable to extract authorizationId", numberTransactions);
            throw new IllegalStateException("Malformed transaction inside payment object for paymentId: " + payment.getId());
        }

        final int numberRelatedResources = payment.getTransactions().get(0).getRelatedResources().size();
        if (numberRelatedResources != 1) {
            log.error("payment had {} number of related resources. Unable to extract authorizationId", numberTransactions);
            throw new IllegalStateException("Malformed related resources inside payment object for paymentId: " + payment.getId());
        }

        return payment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization().getId();
    }

    private void checkForBlackListedWords(@NonNull final String bounty, @NonNull final UUID contractId) {
        final List<String> blackListedBountyWords = getBlackListedWords(bounty);
        if (!blackListedBountyWords.isEmpty()) {
            log.warn("ContractId: {} contains potentially offensive language: {}", contractId, blackListedBountyWords);
        }
    }

    private List<String> getBlackListedWords(@NonNull final String bounty) {
        return Arrays.stream(blackListedWords)
                .filter(blackListedWord -> bounty.toLowerCase().contains(blackListedWord.toLowerCase()))
                .collect(Collectors.toList());
    }
}
