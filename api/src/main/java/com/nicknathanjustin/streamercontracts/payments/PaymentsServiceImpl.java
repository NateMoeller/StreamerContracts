package com.nicknathanjustin.streamercontracts.payments;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PaymentsServiceImpl implements PaymentsService {

    /**
     * Client and secret are for paypal sandbox. Change these values later.
     */
    private static final String CLIENT_ID = "AV_VKTQQlMAB8Fq58souv8_CazzqdHZS5pDF_H9L4eZIIH65I6LjvoSQVBHmgCk9-uJwtpsKxfOX9bop";
    private static final String CLIENT_SECRET = "EBJi9gvGJrKBwWDmIchrgLG2gWUQRAEjbM9jQyojkH16dXm0QUZfZnpMiZPTB1hXF8cFJVxnhUW-te5Q";
    private static final String MODE = "sandbox";
    private static final APIContext API_CONTEXT = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

    @Override
    public Optional<Payment> executePayment(@NonNull final String paymentId) {
        log.info("executing payment for paymentId: {}", paymentId);
        try {
            final Payment payment = Payment.get(API_CONTEXT, paymentId);
            final PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payment.getPayer().getPayerInfo().getPayerId());
            return Optional.of(payment.execute(API_CONTEXT, paymentExecution));
        } catch (PayPalRESTException e) {
            log.error("error executing payment for paymentId: {}. full error: {}", paymentId, e.toString());
        }
        return Optional.empty();
    }

    @Override
    public boolean capturePayment(@NonNull final String authorizationTransactionId) {
        log.info("capturing authorizationTransaction for authorizationTransactionId: {}", authorizationTransactionId);
        try {
            final Authorization authorization = Authorization.get(API_CONTEXT, authorizationTransactionId);
            final Capture capture = new Capture();
            // Note: we cannot reuse the existing Amount from authorization.getAmount(). We need to create a new Amount
            // object to match PayPals API
            final Amount amount = new Amount(authorization.getAmount().getCurrency(), authorization.getAmount().getTotal());
            capture.setAmount(amount);
            return authorization.capture(API_CONTEXT, capture).getState().equals("completed");
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
            final Authorization authorization = Authorization.get(API_CONTEXT, authorizationTransactionId);
            return authorization.doVoid(API_CONTEXT).getState().equals("voided");
        } catch (PayPalRESTException e) {
            log.error("error voiding authorizationTransaction for authorizationTransactionId: {}. full error: {}",
                    authorizationTransactionId, e.toString());
        }
        return false;
    }
}
