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
    private final OrderPaidRestaurantRequestMessagePublisher publisher;
    private final OrderSagaHelper sagaHelper;

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domainEvent = service.payOrder(order, publisher);
        sagaHelper.saveOrder(order);
        log.info("Order with id: {} is paid", order.getId().getValue());
        return domainEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Canceling order with id: {}", paymentResponse.getOrderId());
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        service.cancelOrder(order, paymentResponse.getFailureMessages());
        sagaHelper.saveOrder(order);
        log.info("Order with id: {} is canceled", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }
}
