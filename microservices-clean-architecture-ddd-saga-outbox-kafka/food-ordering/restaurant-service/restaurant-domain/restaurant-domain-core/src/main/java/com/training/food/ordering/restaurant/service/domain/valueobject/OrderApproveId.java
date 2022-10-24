package com.training.food.ordering.restaurant.service.domain.valueobject;

import com.training.food.ordering.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApproveId extends BaseId<UUID> {
    public OrderApproveId(UUID value) {
        super(value);
    }
}
