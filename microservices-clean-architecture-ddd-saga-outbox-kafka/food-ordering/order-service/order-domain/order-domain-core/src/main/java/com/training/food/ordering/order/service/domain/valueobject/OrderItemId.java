package com.training.food.ordering.order.service.domain.valueobject;

import com.training.food.ordering.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {

    public OrderItemId(Long value) {
        super(value);
    }
}
