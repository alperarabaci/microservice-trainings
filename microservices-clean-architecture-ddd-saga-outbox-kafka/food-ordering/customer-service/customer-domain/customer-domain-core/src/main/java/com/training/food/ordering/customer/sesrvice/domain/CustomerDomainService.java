package com.training.food.ordering.customer.sesrvice.domain;

import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import com.training.food.ordering.customer.sesrvice.domain.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);


}
