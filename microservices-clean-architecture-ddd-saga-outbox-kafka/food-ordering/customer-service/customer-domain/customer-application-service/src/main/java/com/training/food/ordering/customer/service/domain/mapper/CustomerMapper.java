package com.training.food.ordering.customer.service.domain.mapper;

import com.training.food.ordering.customer.service.domain.create.CreateCustomerCommand;
import com.training.food.ordering.customer.service.domain.create.CreateCustomerResponse;
import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import com.training.food.ordering.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand command) {
        return new Customer(new CustomerId(command.getCustomerId()),
                command.getUsername(),
                command.getFirstName(),
                command.getLastName()
                );
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }
}
