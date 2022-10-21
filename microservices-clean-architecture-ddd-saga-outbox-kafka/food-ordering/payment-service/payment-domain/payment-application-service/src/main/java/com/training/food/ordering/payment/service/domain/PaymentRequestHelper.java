package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.domain.valueobject.CustomerId;
import com.training.food.ordering.payment.service.domain.dto.PaymentRequest;
import com.training.food.ordering.payment.service.domain.entity.CreditEntry;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;
import com.training.food.ordering.payment.service.domain.entity.Payment;
import com.training.food.ordering.payment.service.domain.event.PaymentEvent;
import com.training.food.ordering.payment.service.domain.exception.PaymentApplicationServiceException;
import com.training.food.ordering.payment.service.domain.exception.PaymentNotFoundException;
import com.training.food.ordering.payment.service.domain.mapper.PaymentDataMapper;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentCanceledMessagePublisher;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.training.food.ordering.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.training.food.ordering.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.training.food.ordering.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper mapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    private final PaymentCompletedMessagePublisher publisher;

    private PaymentCanceledMessagePublisher cancelPublisher;

    private final PaymentFailedMessagePublisher failPublisher;

    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());

        Payment payment = mapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages, publisher);
        persistDbObject(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentNotFoundException("Payment with order id: " +
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages, cancelPublisher);
        persistDbObject(payment, creditEntry, creditHistories, failureMessages);
        return paymentEvent;
    }

    private void persistDbObject(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            String message = String.format("Could not find credit history for customer: %s", customerId.getValue());
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        }
        return creditHistories.get();
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if(creditEntry.isEmpty()) {
            String message = String.format("Could not find credit entry for customer: %s", customerId.getValue());
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        }
        return creditEntry.get();
    }
}
