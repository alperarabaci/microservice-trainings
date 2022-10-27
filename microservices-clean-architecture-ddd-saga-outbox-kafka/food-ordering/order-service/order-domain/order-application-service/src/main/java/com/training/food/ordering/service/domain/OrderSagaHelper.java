package com.training.food.ordering.service.domain;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.exception.OrderNotFoundException;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository repository;

    Order findOrder(String orderId) {
        Optional<Order> order = repository.findById(new OrderId(orderId));
        if(order.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id" + orderId + " could not be found!");
        }
        return order.get();
    }

    void saveOrder(Order order) {
        repository.save(order);
    }

    /**
     * I would prefer orderStatus manage this.
     */
    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case PAID:
                return SagaStatus.PROCESSING;
            case APPROVED:
                return SagaStatus.SUCCEEDED;
            case CANCELLING:
                return SagaStatus.COMPENSATING;
            case CANCELLED:
                return SagaStatus.COMPENSATED;
            default:
                return SagaStatus.STARTED;
        }
    }
}
