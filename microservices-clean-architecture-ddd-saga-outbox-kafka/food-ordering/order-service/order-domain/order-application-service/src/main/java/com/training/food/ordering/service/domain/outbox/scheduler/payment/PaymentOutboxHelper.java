package com.training.food.ordering.service.domain.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.training.food.ordering.service.domain.ports.output.repository.PaymentOutboxRepository;
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
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository repository;

    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        return repository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses
        );
    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID sagaId,
                                                                                            SagaStatus... sagaStatuses){
        return repository.findByTypeAndOSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }


    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = repository.save(orderPaymentOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId());
            throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id: " +
                    orderPaymentOutboxMessage.getId());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId());
    }

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload payload,
                                         OrderStatus orderStatus,
                                         SagaStatus sagaStatus,
                                         OutboxStatus outboxStatus,
                                         UUID sagaId) {
        OrderPaymentOutboxMessage message = OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(payload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(payload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build();
        save(message);
    }

    private String createPayload(OrderPaymentEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Could not create OrderPaymentEventPayload object for order id: {}",
                    payload.getOrderId(), e);
            throw new OrderDomainException("Could not create OrderPaymentEventPayload object for order id" +
                    payload.getOrderId(), e);
        }
    }

    @Transactional
    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                      SagaStatus... sagaStatuses) {
        repository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }
}
