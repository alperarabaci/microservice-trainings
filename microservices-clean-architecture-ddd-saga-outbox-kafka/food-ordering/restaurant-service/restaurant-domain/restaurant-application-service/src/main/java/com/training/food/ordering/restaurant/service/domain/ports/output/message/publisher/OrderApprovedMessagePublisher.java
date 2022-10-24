package com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovalEvent> {
}
