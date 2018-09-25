package com.nicknathanjustin.streamercontracts.payments;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PaymentsServiceImplTest {

    @InjectMocks
    private PaymentsServiceImpl paymentsService;

    @Test(expected = NullPointerException.class)
    public void executePayment_nullInput_throwsException() {
        paymentsService.executePayment(null);
    }
}
