package com.training.food.ordering.payment.service.domain.entity;

import com.training.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID value) {
        super(value);
    }

}
