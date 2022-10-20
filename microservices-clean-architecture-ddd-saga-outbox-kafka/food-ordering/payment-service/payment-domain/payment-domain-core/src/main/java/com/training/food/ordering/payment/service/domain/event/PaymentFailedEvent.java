package com.training.food.ordering.payment.service.domain.event;

import com.training.food.order.domain.event.DomainEvent;
import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {
    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        super(payment, createdAt, failureMessages);
    }
}
