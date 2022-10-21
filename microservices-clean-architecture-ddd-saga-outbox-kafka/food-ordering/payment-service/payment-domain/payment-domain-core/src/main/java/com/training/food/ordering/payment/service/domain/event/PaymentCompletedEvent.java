package com.training.food.ordering.payment.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentCompletedEvent> publisher;
    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCompletedEvent> publisher) {
        super(payment, createdAt, Collections.emptyList());
        this.publisher = publisher;
    }

    public static PaymentCompletedEvent createdAtNow(Payment payment, DomainEventPublisher<PaymentCompletedEvent> publisher) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UCT"));
        return new PaymentCompletedEvent(payment, now, publisher);
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
