package com.training.food.ordering.service.domain.ports.output.repository;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);

    Optional<Order> findById(OrderId orderId);
}
