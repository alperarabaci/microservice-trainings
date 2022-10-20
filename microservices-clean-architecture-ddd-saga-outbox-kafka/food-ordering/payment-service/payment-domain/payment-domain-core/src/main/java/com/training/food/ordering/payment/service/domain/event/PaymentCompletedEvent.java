package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent{
    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }

    public static PaymentCompletedEvent createdAtNow(Payment payment) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCompletedEvent(payment, now);
    }
}
