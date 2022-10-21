package com.training.food.ordering.service.domain;

import com.training.food.ordering.service.domain.dto.track.TrackOrderQuery;
import com.training.food.ordering.service.domain.dto.track.TrackOrderResponse;
import com.training.food.ordering.service.domain.mapper.OrderDataMapper;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.exception.OrderNotFoundException;
import com.training.food.ordering.order.service.domain.valueobject.TrackingId;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper mapper;
    private final OrderRepository repository;
    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderResult = repository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if(orderResult.isEmpty()) {
            log.warn("Cound not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Cound not find order with tracking id: "+ trackOrderQuery.getOrderTrackingId());
        }
        return mapper.orderToTrackOrderResponse(orderResult.get());
    }
}
