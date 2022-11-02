package com.training.food.ordering.payment.service.dataaccess.outbox.repository;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.domain.valueobject.PaymentStatus;
import com.training.food.ordering.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {

    Optional<List<OrderOutboxEntity>> findByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

    Optional<OrderOutboxEntity> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                    UUID sagaId,
                                                                    PaymentStatus paymentStatus,
                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus outboxStatus);

}
