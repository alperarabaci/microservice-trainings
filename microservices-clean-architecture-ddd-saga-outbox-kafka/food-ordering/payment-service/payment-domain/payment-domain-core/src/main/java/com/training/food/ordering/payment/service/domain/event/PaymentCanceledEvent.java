package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCanceledEvent extends PaymentEvent{
    public PaymentCanceledEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        super(payment, createdAt, failureMessages);
    }

    public static PaymentCanceledEvent createdAtNow(Payment payment) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCanceledEvent(payment, now, Collections.emptyList());
    }

    public static PaymentCanceledEvent createdAtNow(Payment payment, List<String> failureMessages) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCanceledEvent(payment, now, failureMessages);
    }
}
