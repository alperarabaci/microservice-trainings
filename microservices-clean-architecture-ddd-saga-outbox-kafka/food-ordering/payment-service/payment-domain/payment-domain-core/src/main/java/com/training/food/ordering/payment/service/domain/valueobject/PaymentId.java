package com.training.food.ordering.payment.service.domain.valueobject;

import com.training.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID value) {
        super(value);
    }
}
