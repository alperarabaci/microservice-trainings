package com.training.food.ordering.customer.sesrvice.domain.event;

import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import com.training.food.ordering.domain.event.DomainEvent;

import java.time.ZonedDateTime;

public class CustomerCreatedEvent implements DomainEvent<Customer> {

    private final Customer customer;

    private final ZonedDateTime createAt;

    public CustomerCreatedEvent(Customer customer, ZonedDateTime createAt) {
        this.customer = customer;
        this.createAt = createAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }
}
