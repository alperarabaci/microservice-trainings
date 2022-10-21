package com.training.food.ordering.order.service.domain.event;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> publisher;

    public OrderCancelledEvent(Order order,
                               ZonedDateTime createdAt,
                               DomainEventPublisher<OrderCancelledEvent> publisher) {
        super(order, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}

