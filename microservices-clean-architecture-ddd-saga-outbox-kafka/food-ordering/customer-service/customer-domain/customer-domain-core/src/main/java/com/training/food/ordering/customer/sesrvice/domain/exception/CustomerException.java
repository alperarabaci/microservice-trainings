package com.training.food.ordering.customer.sesrvice.domain.exception;

import com.training.food.ordering.domain.exception.DomainException;

public class CustomerException extends DomainException {
    public CustomerException(String message) {
        super(message);
    }
}
