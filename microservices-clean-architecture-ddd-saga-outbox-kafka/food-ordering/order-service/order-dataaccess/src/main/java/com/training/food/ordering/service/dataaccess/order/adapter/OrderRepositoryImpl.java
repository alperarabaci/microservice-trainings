package com.training.food.ordering.service.dataaccess.order.adapter;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.valueobject.TrackingId;
import com.training.food.ordering.service.dataaccess.order.entity.OrderEntity;
import com.training.food.ordering.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.training.food.ordering.service.dataaccess.order.repository.OrderJpaRepository;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {


    private final OrderDataAccessMapper mapper;
    private final OrderJpaRepository repository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.orderToOrderEntity(order);
        return mapper.orderEntityToOrder(repository.save(entity));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return repository.findByTrackingId(trackingId.getValue())
                .map(mapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return repository.findById(orderId)
                .map(mapper::orderEntityToOrder);
    }
}
