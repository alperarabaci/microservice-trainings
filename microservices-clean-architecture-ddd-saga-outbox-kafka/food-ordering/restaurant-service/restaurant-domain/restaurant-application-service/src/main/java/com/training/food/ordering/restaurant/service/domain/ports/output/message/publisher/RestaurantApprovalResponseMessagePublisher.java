package com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher;


import com.training.food.ordering.outbox.OutboxStatus;
import com.training.food.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
