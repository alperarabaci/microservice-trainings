package com.training.food.ordering.customer.service.domain.ports.input.service;

import com.training.food.ordering.customer.service.domain.create.CreateCustomerCommand;
import com.training.food.ordering.customer.service.domain.create.CreateCustomerResponse;

import javax.validation.Valid;

public interface CustomerApplicationService {

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}
