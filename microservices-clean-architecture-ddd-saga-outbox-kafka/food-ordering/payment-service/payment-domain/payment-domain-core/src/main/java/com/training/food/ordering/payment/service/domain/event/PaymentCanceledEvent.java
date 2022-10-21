package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCanceledEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCanceledEvent> publisher;

    public PaymentCanceledEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentCanceledEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }

    public static PaymentCanceledEvent createdAtNow(Payment payment, DomainEventPublisher<PaymentCanceledEvent> publisher) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCanceledEvent(payment, now, Collections.emptyList(), publisher);
    }

    public static PaymentCanceledEvent createdAtNow(Payment payment, List<String> failureMessages, DomainEventPublisher<PaymentCanceledEvent> publisher) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCanceledEvent(payment, now, failureMessages, publisher);
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
