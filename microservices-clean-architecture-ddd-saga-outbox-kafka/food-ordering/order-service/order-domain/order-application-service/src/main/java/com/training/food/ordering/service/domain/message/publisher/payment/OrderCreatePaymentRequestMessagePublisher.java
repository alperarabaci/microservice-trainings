package com.training.food.ordering.service.domain.message.publisher.payment;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
