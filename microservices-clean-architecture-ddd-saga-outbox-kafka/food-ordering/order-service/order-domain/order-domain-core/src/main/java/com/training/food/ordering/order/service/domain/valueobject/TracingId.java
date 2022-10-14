package com.training.food.ordering.order.service.domain.valueobject;

import com.training.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class TracingId extends BaseId<UUID> {
    public TracingId(UUID value) {
        super(value);
    }
}
