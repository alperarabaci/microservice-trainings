package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.payment.service.domain.dto.PaymentRequest;
import com.training.food.ordering.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    /**
     * Refactoring: Replaced with => event.fire();
     */
    /*
    private final PaymentCompletedMessagePublisher completedMessagePublisher;
    private final PaymentCancelledMessagePublisher cancelledMessagePublisher;
    private final PaymentFailedMessagePublisher failedMessagePublisher;
    */

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistCancelPayment(paymentRequest);
    }


}
