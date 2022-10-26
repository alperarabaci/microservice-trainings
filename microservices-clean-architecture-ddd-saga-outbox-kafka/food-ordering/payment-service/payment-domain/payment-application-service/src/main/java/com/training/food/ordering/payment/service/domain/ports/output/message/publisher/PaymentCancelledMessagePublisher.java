package com.training.food.ordering.payment.service.domain.ports.output.message.publisher;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {

}
