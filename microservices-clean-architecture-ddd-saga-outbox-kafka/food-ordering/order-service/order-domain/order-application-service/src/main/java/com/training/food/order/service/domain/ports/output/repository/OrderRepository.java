package com.training.food.order.service.domain.ports.output.repository;

import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.valueobject.TracingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingId(TracingId tracingId);

}
