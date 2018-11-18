package com.nicknathanjustin.streamercontracts.payments;

import com.paypal.api.payments.Payment;

import java.util.Optional;

public interface PaymentsService{

    /**
     * Executes a PayPal payment that the payer has approved. This payment is valid for 3 days and can be captured or
     * voided while valid.
     *
     * @param paymentId Paypal paymentId
     * @return A payment object if successful. Optional of null otherwise.
     */
    Optional<Payment> executePayment(String paymentId);

    /**
     * Captures a previously executed payment
     *
     * @param authorizationTransactionId Id retrieved from calling executePayment
     * @return true if payment was captured. False otherwise
     */
    boolean capturePayment(String authorizationTransactionId);

    /**
     * Voids a previously executed payment
     *
     * @param authorizationTransactionId Id retrieved from calling executePayment
     * @return true if payment was voided. False otherwise
     */
    boolean voidPayment(String authorizationTransactionId);
}
