package com.training.food.ordering.payment.service.domain.valueobject;

import com.training.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntityId extends BaseId<UUID> {

    public CreditEntityId(UUID value) {
        super(value);
    }
}
