package com.training.food.ordering.payment.service.domain.ports.output.repository;

import com.training.food.ordering.domain.valueobject.PaymentStatus;
import com.training.food.ordering.outbox.OutboxStatus;
import com.training.food.ordering.payment.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderOutboxRepository {

    OrderOutboxMessage save(OrderOutboxMessage message);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type,
                                                                 OutboxStatus outboxStatus);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                                    UUID sagaId,
                                                                                    PaymentStatus paymentStatus,
                                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}
