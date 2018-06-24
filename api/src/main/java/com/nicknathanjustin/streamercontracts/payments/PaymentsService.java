package com.nicknathanjustin.streamercontracts.payments;

import com.paypal.api.payments.Payment;

import java.util.Optional;

public interface PaymentsService{

    Optional<Payment> executePayment(String paymentId);

    boolean capturePayment(String authorizationTransactionId);

    boolean voidPayment(String authorizationTransactionId);
}
