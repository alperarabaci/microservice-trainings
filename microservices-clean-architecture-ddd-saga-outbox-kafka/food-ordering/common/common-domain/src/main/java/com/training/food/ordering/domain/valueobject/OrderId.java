package com.training.food.ordering.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {
    public OrderId(UUID value) {
        super(value);
    }

    public OrderId(String value) {
        super(UUID.fromString(value));
    }
}
