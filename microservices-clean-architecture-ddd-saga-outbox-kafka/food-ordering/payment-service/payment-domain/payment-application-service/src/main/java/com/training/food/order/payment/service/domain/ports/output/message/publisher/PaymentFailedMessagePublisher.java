package com.training.food.order.payment.service.domain.ports.output.message.publisher;

import com.training.food.order.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent> {

}
