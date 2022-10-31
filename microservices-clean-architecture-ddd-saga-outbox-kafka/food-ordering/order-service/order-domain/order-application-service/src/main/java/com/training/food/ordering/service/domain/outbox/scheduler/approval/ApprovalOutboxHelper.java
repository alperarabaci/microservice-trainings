package com.training.food.ordering.service.domain.outbox.scheduler.approval;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.training.food.ordering.service.domain.ports.output.repository.ApprovalOutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.training.food.ordering.saga.order.SagaConstant.ORDER_SAGA_NAME;

@Slf4j
@Component
@AllArgsConstructor
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository repository;

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return repository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatus);
    }


    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId, SagaStatus... sagaStatus) {
        return repository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId, sagaStatus);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        OrderApprovalOutboxMessage response = repository.save(orderApprovalOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderApprovalOutboxMessage with outbox id: {}",
                    orderApprovalOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderApprovalOutboxMessage with outbox id: " +
                    orderApprovalOutboxMessage.getId());
        }
        log.info("OrderApprovalOutboxMessage saved with outbox id: {}", orderApprovalOutboxMessage.getId());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus) {
        repository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);
    }


}
