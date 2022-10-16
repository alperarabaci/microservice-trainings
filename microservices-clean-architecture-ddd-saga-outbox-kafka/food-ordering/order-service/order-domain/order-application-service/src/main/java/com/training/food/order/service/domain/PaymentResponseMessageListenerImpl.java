package com.training.food.order.service.domain;

import com.training.food.order.service.domain.dto.message.PaymentResponse;
import com.training.food.order.service.domain.ports.input.message.listener.payment.PaymentMessageResponseListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@Slf4j
public class PaymentResponseMessageListenerImpl implements PaymentMessageResponseListener {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCanceled(PaymentResponse paymentResponse) {

    }
}
