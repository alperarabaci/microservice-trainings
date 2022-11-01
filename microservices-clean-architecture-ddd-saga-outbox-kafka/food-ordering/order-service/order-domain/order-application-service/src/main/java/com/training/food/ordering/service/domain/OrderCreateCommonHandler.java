package com.training.food.ordering.service.domain;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.service.domain.dto.create.CreateOrderCommand;
import com.training.food.ordering.service.domain.dto.create.CreateOrderResponse;
import com.training.food.ordering.service.domain.mapper.OrderDataMapper;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.training.food.ordering.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommonHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final PaymentOutboxHelper paymentOutboxHelper;

    private final OrderSagaHelper orderSagaHelper;


    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent event = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {},", event.getOrder().getId().getValue());

        CreateOrderResponse response = orderDataMapper.orderToCreateOrderResponse(event.getOrder(), "Order created successfully.");

        OrderPaymentEventPayload payload = orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(event);
        SagaStatus orderStatus = orderSagaHelper.orderStatusToSagaStatus(event.getOrder().getOrderStatus());
        paymentOutboxHelper.savePaymentOutboxMessage(payload,
                event.getOrder().getOrderStatus(),
                orderStatus,
                OutboxStatus.STARTED,
                UUID.randomUUID());

        log.info("Returning CreateOrderResponse with order id: {}", event.getOrder().getId());

        return response;
    }

}
