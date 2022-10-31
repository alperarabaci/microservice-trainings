package com.training.food.ordering.service.domain.outbox.scheduler.payment;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.saga.SagaStatus;
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
    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                      SagaStatus... sagaStatuses) {
        repository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }
}
