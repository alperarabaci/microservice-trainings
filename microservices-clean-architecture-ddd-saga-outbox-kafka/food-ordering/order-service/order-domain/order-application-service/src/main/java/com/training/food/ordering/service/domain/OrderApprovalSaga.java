package com.training.food.ordering.service.domain;

import com.training.food.ordering.domain.event.EmptyEvent;
import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.event.OrderCancelledEvent;
import com.training.food.ordering.saga.SagaStep;
import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.service.domain.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService service;
    private final OrderSagaHelper sagaHelper;
    private final OrderCancelledPaymentRequestMessagePublisher publisher;

    @Transactional
    @Override
    public EmptyEvent process(RestaurantApprovalResponse response) {
        log.info("Approving order with id: {}", response.getOrderId());
        Order order = sagaHelper.findOrder(response.getOrderId());
        service.approveOrder(order);
        sagaHelper.saveOrder(order);
        log.info("Order with id: {} is approved", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Transactional
    @Override
    public OrderCancelledEvent rollback(RestaurantApprovalResponse response) {
        log.info("Cancelling order with id: {}", response.getOrderId());
        Order order = sagaHelper.findOrder(response.getOrderId());
        OrderCancelledEvent domainEvent = service.cancelOrderPayment(order,
                response.getFailureMessages(),
                publisher);
        sagaHelper.saveOrder(order);
        log.info("Order with id: {} is canceling", order.getId().getValue());
        return domainEvent;
    }
}
