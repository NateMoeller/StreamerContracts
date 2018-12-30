package com.nicknathanjustin.streamercontracts.payments;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Slf4j
public class PaymentsServiceImpl implements PaymentsService {

    @Value("${paypal.mode}")
    private static String MODE;
    @Value("${paypal.clientId}")
    private static String CLIENT_ID;
    @Value("${paypal.clientSecret}")
    private static String CLIENT_SECRET;

    @Override
    public Optional<Payment> executePayment(@NonNull final String paymentId) {
        log.info("executing payment for paymentId: {}", paymentId);
        try {
            final Payment payment = Payment.get(getAPIContext(), paymentId);
            final PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
            return Optional.of(payment.execute(getAPIContext(), paymentExecution));
        } catch (PayPalRESTException e) {
            log.error("error executing payment for paymentId: {}. full error: {}", paymentId, e.toString());
        }
        return Optional.empty();
    }

    @Override
    public boolean capturePayment(@NonNull final String authorizationTransactionId) {
        log.info("capturing authorizationTransaction for authorizationTransactionId: {}", authorizationTransactionId);
        try {
            final Authorization authorization = Authorization.get(getAPIContext(), authorizationTransactionId);
            final Capture capture = new Capture();
            // Note: we cannot reuse the existing Amount from authorization.getAmount(). We need to create a new Amount
            // object to match PayPals API
            final Amount amount = new Amount(authorization.getAmount().getCurrency(), authorization.getAmount().getTotal());
            capture.setAmount(amount);
            return authorization.capture(getAPIContext(), capture).getState().equals("completed");
        } catch (PayPalRESTException e) {
            log.error("error capturing authorizationTransaction for authorizationTransactionId: {}. full error: {}",
                    authorizationTransactionId, e.toString());
        }
        return false;
    }

    @Override
    public boolean voidPayment(@NonNull final String authorizationTransactionId) {
        log.info("voiding authorizationTransaction for authorizationTransactionId: {}", authorizationTransactionId);
        try {
            final Authorization authorization = Authorization.get(getAPIContext(), authorizationTransactionId);
            return authorization.doVoid(getAPIContext()).getState().equals("voided");
        } catch (PayPalRESTException e) {
            log.error("error voiding authorizationTransaction for authorizationTransactionId: {}. full error: {}",
                    authorizationTransactionId, e.toString());
        }
        return false;
    }

    private APIContext getAPIContext() {
        //NOTE: a new APIContext is needed for each paypal API call
        return new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
    }
}
