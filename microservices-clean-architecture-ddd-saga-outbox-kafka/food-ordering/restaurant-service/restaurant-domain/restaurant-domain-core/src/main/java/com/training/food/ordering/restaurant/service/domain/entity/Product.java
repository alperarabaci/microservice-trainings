package com.training.food.ordering.restaurant.service.domain.entity;

import com.training.food.ordering.domain.entity.BaseEntity;
import com.training.food.ordering.domain.valueobject.Money;
import com.training.food.ordering.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId > {

    private String name;
    private Money price;
    private int quantity;
    private boolean available;

    public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    private Product(Builder builder) {
        setId(builder.id);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        available = builder.available;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId id;
        private String name;
        private Money price;
        private int quantity;
        private boolean available;

        private Builder() {
        }



        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
