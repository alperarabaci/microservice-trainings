package com.training.food.ordering.payment.service.domain.ports.output.message.publisher;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {

    void publish(OrderOutboxMessage outboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);

}
