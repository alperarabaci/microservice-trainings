package com.training.food.ordering.order.service.domain.exception;

import com.training.food.order.domain.exception.DomainException;

public class OrderDomainException extends DomainException {


    public OrderDomainException(String message) {
         super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
