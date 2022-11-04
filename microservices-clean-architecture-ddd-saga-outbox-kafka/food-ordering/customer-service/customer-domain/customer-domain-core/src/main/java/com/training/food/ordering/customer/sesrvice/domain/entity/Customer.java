package com.training.food.ordering.customer.sesrvice.domain.entity;

import com.training.food.ordering.domain.entity.AggregateRoot;
import com.training.food.ordering.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    private final String username;
    private final String firstName;
    private final String lastName;

    public Customer(CustomerId customerId, String username, String firstName, String lastName) {
        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
