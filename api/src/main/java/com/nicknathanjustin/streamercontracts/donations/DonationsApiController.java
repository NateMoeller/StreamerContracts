package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping(path = "/execute", method = RequestMethod.POST)
    public String executePayment(@RequestBody @NonNull final CreateDonationRequest createDonationRequest) {
        final Payment payment = paymentsService.executePayment(createDonationRequest.getPayPalPaymentId()).orElse(null);
        if(payment == null) {
            return "couldn't execute payment for id: " + createDonationRequest.getPayPalPaymentId();
        }
        return payment.toJSON();
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
