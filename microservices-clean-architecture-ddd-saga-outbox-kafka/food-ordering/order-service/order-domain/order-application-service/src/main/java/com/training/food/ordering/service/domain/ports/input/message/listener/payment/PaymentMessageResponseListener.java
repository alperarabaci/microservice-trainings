package com.training.food.ordering.service.domain.ports.input.message.listener.payment;

import com.training.food.ordering.service.domain.dto.message.PaymentResponse;

public interface PaymentMessageResponseListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCanceled(PaymentResponse paymentResponse);
}
