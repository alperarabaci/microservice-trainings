package com.training.food.ordering.order.service.domain.valueobject;

import com.training.food.ordering.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
