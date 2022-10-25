package com.training.food.ordering.service.domain;

import com.training.food.ordering.domain.event.DomainEvent;
import com.training.food.ordering.domain.event.EmptyEvent;
import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import com.training.food.ordering.order.service.domain.exception.OrderNotFoundException;
import com.training.food.ordering.saga.SagaStep;
import com.training.food.ordering.service.domain.dto.message.PaymentResponse;
import com.training.food.ordering.service.domain.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService service;
    private final OrderRepository repository;
    private final OrderPaidRestaurantRequestMessagePublisher publisher;

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
        Order order = findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domainEvent = service.payOrder(order);
        repository.save(order);
        log.info("Order with id: {} is paid", order.getId().getValue());
        return domainEvent;
    }

    private Order findOrder(String orderId) {
        Optional<Order> order = repository.findById(new OrderId(orderId));
        if(order.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id" + orderId + " could not be found!");
        }
        return order.get();
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Canceling order with id: {}", paymentResponse.getOrderId());
        Order order = findOrder(paymentResponse.getOrderId());
        service.cancelOrder(order, paymentResponse.getFailureMessages());
        repository.save(order);
        log.info("Order with id: {} is canceled", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }
}
