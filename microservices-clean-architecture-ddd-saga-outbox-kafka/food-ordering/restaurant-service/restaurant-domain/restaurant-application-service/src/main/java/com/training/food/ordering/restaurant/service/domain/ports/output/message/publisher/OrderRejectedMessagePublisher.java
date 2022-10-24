package com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.restaurant.service.domain.event.OrderRejectedEvent;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
