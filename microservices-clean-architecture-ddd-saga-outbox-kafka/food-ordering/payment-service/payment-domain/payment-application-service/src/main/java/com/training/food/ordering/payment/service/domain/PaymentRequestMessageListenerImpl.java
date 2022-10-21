package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.payment.service.domain.dto.PaymentRequest;
import com.training.food.ordering.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;

public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {


    @Override
    public void completePayment(PaymentRequest paymentRequest) {

    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {

    }
}
