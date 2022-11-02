package com.training.food.ordering.payment.service.domain.outbox.scheduler;

import com.training.food.order.outbox.OutboxScheduler;
import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.training.food.ordering.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper paymentOutboxHelper;

    @Scheduled(cron = "@midnight")
    @Transactional
    @Override
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> response = paymentOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (response.isPresent()) {
            List<OrderOutboxMessage> outboxMessages = response.get();
            log.info("Received {} OrderOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            paymentOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(
                    OutboxStatus.COMPLETED);
            log.info("{} OrderOutboxMessage deleted!", outboxMessages.size());
        }
    }


}