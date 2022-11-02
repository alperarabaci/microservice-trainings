package com.training.food.ordering.service.domain.ports.output.message.publisher.restaurantapproval;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);
}
