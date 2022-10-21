package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentFailedEvent> publisher;

    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentFailedEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
