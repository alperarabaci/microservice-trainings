package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCancelledEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCancelledEvent> publisher;

    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }

    public static PaymentCancelledEvent createdAtNow(Payment payment, DomainEventPublisher<PaymentCancelledEvent> publisher) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCancelledEvent(payment, now, Collections.emptyList(), publisher);
    }

    public static PaymentCancelledEvent createdAtNow(Payment payment, List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> publisher) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCancelledEvent(payment, now, failureMessages, publisher);
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
