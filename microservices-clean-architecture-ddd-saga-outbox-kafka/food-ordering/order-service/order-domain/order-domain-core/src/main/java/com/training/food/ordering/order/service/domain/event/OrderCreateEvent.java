package com.training.food.ordering.order.service.domain.event;

import com.training.food.ordering.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreateEvent extends OrderEvent {

    public OrderCreateEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
