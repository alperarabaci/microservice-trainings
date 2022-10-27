package com.training.food.ordering.service.domain.message.publisher.payment;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage outboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);

}
