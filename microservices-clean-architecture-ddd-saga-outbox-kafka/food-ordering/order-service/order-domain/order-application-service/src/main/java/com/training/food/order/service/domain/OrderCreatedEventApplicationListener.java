package com.training.food.order.service.domain;

import com.training.food.order.service.domain.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreatedEventApplicationListener {

    private final OrderCreatedPaymentRequestMessagePublisher publisher;

    @TransactionalEventListener
    void process(OrderCreatedEvent event) {
        publisher.publish(event);
    }
}
