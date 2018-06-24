package com.nicknathanjustin.streamercontracts.donations;

import com.nicknathanjustin.streamercontracts.payments.PaymentsService;
import com.paypal.api.payments.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Slf4j
public class DonationsApiController {

    @NonNull private final PaymentsService paymentsService;

    //TODO: turn these all into POST requests once the CSRF issue is fixed
    @RequestMapping(path = "/execute/{paymentId}", method = RequestMethod.GET)
    public String executePayment(@PathVariable("paymentId") @NonNull final String paymentId) {
        final Payment payment = paymentsService.executePayment(paymentId).orElse(null);
        if(payment == null) {
            return "couldn't execute payment for id: " + paymentId;
        }
        return payment.toJSON();
    }

    @RequestMapping(path = "/capture/{authorizationId}", method = RequestMethod.GET)
    public boolean capturePayment(@PathVariable("authorizationId") @NonNull final String authorizationId) {
        return paymentsService.capturePayment(authorizationId);
    }

    @RequestMapping(path = "/void/{authorizationId}", method = RequestMethod.GET)
    public boolean voidPayment(@PathVariable("authorizationId") @NonNull final String authorizationId) {
        return paymentsService.voidPayment(authorizationId);
    }
}
