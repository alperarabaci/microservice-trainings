package com.training.food.ordering.customer.service.domain.ports.output.repository;

import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);

}
