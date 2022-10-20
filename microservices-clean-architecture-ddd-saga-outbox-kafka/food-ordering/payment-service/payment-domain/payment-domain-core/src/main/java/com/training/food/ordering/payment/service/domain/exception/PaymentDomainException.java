package com.training.food.ordering.payment.service.domain.exception;

import com.training.food.order.domain.exception.DomainException;

public class PaymentDomainException extends DomainException {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
