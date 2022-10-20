package com.training.food.ordering.payment.service.domain.entity;

import com.training.food.order.domain.entity.BaseEntity;
import com.training.food.order.domain.valueobject.CustomerId;
import com.training.food.order.domain.valueobject.Money;
import com.training.food.ordering.payment.service.domain.valueobject.CreditEntityId;

public class CreditEntity extends BaseEntity<CreditEntityId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    private CreditEntity(Builder builder) {
        setId(builder.id);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public void addCreditAmount(Money amount) {
        totalCreditAmount = totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        totalCreditAmount = totalCreditAmount.subtract(amount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }


    public static final class Builder {
        private CreditEntityId id;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CreditEntityId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntity build() {
            return new CreditEntity(this);
        }
    }
}