package com.training.food.ordering.order.service.domain.entity;

import com.training.food.ordering.domain.entity.BaseEntity;
import com.training.food.ordering.domain.valueobject.Money;
import com.training.food.ordering.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }


    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public void updateWithConfirmedNamedAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;

    }
}
