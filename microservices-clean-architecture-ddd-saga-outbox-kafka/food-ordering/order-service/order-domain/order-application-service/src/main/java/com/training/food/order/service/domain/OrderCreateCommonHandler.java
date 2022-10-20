package com.training.food.order.service.domain;

import com.training.food.order.service.domain.dto.create.CreateOrderCommand;
import com.training.food.order.service.domain.dto.create.CreateOrderResponse;
import com.training.food.order.service.domain.mapper.OrderDataMapper;
import com.training.food.order.service.domain.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.training.food.ordering.order.service.domain.event.OrderCreateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommonHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher publisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreateEvent event = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {},", event.getOrder().getId().getValue());
        publisher.publish(event);
        return orderDataMapper.orderToCreateOrderResponse(event.getOrder(), "Order created successfully.");
    }

}
