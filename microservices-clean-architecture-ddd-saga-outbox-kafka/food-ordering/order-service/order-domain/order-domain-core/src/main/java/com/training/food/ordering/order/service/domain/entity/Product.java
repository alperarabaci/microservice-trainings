package com.training.food.ordering.order.service.domain.entity;

import com.training.food.order.domain.entity.BaseEntity;
import com.training.food.order.domain.valueobject.Money;
import com.training.food.order.domain.valueobject.ProductId;

import java.util.UUID;

public class Product extends BaseEntity<ProductId> {

    private final String name;
    private final Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
