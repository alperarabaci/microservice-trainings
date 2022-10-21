package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.payment.service.domain.entity.CreditEntry;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;
import com.training.food.ordering.payment.service.domain.entity.Payment;
import com.training.food.ordering.payment.service.domain.event.PaymentCanceledEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages,
                                            DomainEventPublisher<PaymentCompletedEvent> publisher);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages,
                                          DomainEventPublisher<PaymentCanceledEvent> publisher);
}
