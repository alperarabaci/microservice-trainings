package com.training.food.ordering.restaurant.service.domain.valueobject;

import com.training.food.ordering.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value) {
        super(value);
    }
}
