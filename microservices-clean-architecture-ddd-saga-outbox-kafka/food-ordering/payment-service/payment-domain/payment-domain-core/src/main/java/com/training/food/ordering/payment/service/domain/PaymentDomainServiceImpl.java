package com.training.food.ordering.payment.service.domain;

import com.training.food.order.domain.valueobject.Money;
import com.training.food.order.domain.valueobject.PaymentStatus;
import com.training.food.ordering.payment.service.domain.entity.CreditEntity;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;
import com.training.food.ordering.payment.service.domain.entity.CreditHistoryId;
import com.training.food.ordering.payment.service.domain.entity.Payment;
import com.training.food.ordering.payment.service.domain.event.PaymentCanceledEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentEvent;
import com.training.food.ordering.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {


    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntity creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initialize();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
        } else {
            log.info("Payment initiation is failed for order id: {}", payment.getOrderId());
            payment.updateStatus(PaymentStatus.FAILED);
        }
        return PaymentCompletedEvent.createdAtNow(payment);
    }

    private void validateCreditHistory(CreditEntity creditEntry,
                                       List<CreditHistory> creditHistories,
                                       List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (!totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            String message = String.format("Customer with id: %s doesn't have enough credit according to credit history",
                    creditEntry.getCustomerId().getValue());
            log.error(message);
            failureMessages.add(message);
        }
        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalCreditHistory))) {
            String message = String.format("Credit history total is not equal to current credit for customer id: %s", creditEntry.getCustomerId().getValue());
            failureMessages.add(message);
        }
    }

    private static Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType credit) {
        return creditHistories.stream()
                .filter(creditHistory -> credit == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransactionType transactionType) {

        CreditHistory creditHistory = CreditHistory.builder()
                .id(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build();
        creditHistories.add(creditHistory);
    }


    private void subtractCreditEntry(Payment payment, CreditEntity creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void validateCreditEntry(Payment payment, CreditEntity creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id: {} doesn't have enough credit for payment!",
                    payment.getCustomerId().getValue());
            String message = "Customer with %s doesn't have enough credit for payment";
            failureMessages.add(String.format(message, payment.getCustomerId().getValue()));
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntity creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {

        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return PaymentCanceledEvent.createdAtNow(payment);
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return PaymentCanceledEvent.createdAtNow(payment, failureMessages);
        }

    }

    private void addCreditEntry(Payment payment, CreditEntity creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
