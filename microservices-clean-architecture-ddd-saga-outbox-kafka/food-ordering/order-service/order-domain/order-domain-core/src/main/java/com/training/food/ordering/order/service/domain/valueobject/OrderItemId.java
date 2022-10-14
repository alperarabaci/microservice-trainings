package com.training.food.ordering.order.service.domain.valueobject;

import com.training.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderItemId extends BaseId<Long> {

    public OrderItemId(Long value) {
        super(value);
    }
}
