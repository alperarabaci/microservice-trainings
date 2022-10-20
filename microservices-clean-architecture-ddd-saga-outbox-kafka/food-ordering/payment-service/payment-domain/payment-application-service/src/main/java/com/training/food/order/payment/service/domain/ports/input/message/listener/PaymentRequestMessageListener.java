package com.training.food.order.payment.service.domain.ports.input.message.listener;

import com.training.food.order.payment.service.domain.dto.PaymentRequest;

public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
