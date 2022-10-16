package com.training.food.order.service.domain.message.publisher.restaurantapproval;

import com.training.food.order.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
