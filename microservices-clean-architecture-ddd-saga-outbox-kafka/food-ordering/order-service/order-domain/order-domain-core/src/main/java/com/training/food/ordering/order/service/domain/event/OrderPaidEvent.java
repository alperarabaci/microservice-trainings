package com.training.food.ordering.order.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> publisher;

    public OrderPaidEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderPaidEvent> publisher) {
        super(order, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
