package com.training.food.ordering.order.service.domain.event;

import com.training.food.order.domain.event.DomainEvent;
import com.training.food.ordering.order.service.domain.entity.Order;

public abstract class OrderEvent implements DomainEvent<Order> {
}
