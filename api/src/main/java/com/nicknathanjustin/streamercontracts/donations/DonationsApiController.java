package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractService;
import com.nicknathanjustin.streamercontracts.donations.Requests.CreateDonationRequest;
import com.nicknathanjustin.streamercontracts.donations.Requests.UpdateDonationRequest;
import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import com.nicknathanjustin.streamercontracts.users.UserService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationsApiController {

    @NonNull private final PaymentsService paymentsService;

    @NonNull private final ContractService contractService;

    @NonNull private final UserService userService;

    @NonNull private final DonationService donationService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createDonation(@RequestBody @NonNull final CreateDonationRequest createDonationRequest) {
        final Payment payment = paymentsService.executePayment(createDonationRequest.getPayPalPaymentId()).orElse(null);
        if(payment == null) {
            log.warn("Couldn't execute payment for id: " + createDonationRequest.getPayPalPaymentId());
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final UserModel proposer = userService.getUser(createDonationRequest.getUsername()).orElse(null);
        final UserModel streamer = userService.getUser(createDonationRequest.getStreamerUsername()).orElse(null);
        if (proposer == null || streamer == null) {
            log.warn("Username: " + createDonationRequest.getUsername() + " or StreamerUsername: " + createDonationRequest.getStreamerUsername() + " does not exist.");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final ContractModel contract = contractService.createContract(proposer, streamer, null, createDonationRequest.getBounty());
        donationService.createDonation(contract, proposer, createDonationRequest.getAmount(), contract.getProposedAt(), createDonationRequest.getPayPalPaymentId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity updateDonation(@RequestBody @NonNull final UpdateDonationRequest updateDonationRequest) {
        final DonationModel donationModel = donationService.getDonation(updateDonationRequest.getDonationId()).orElse(null);
        if (donationModel == null) {
            log.warn("No donation found for id: " + updateDonationRequest.getDonationId());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/listOpenDonations/{page}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity listDonations(@Nullable final OAuth2Authentication authentication,
                                        @PathVariable final int page,
                                        @PathVariable final int pageSize) {
        if (authentication == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        final Pageable pageable = PageRequest.of(page, pageSize);
        final UserModel userModel = userService.getUserFromAuthContext(authentication);
        final Page<OpenDonationDto> donationsWithOpenContracts = donationService.listOpenDonations(userModel.getId(), pageable);
        return ResponseEntity.ok(donationsWithOpenContracts);
    }
}
