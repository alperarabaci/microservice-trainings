package com.training.food.order.service.domain.ports.input.message.listener.payment;

import com.training.food.order.service.domain.dto.message.PaymentResponse;

public interface PaymentMessageResponseListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCanceled(PaymentResponse paymentResponse);
}
