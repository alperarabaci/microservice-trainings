package com.training.food.order.service.domain.message.publisher.payment;

import com.training.food.order.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
