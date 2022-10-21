package com.training.food.ordering.payment.service.domain.valueobject;

import com.training.food.ordering.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

    public CreditEntryId(UUID value) {
        super(value);
    }
}
