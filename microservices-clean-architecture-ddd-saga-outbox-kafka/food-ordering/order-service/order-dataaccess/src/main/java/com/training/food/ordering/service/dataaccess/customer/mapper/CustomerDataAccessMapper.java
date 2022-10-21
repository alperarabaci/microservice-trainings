package com.training.food.ordering.service.dataaccess.customer.mapper;


import com.training.food.ordering.domain.valueobject.CustomerId;
import com.training.food.ordering.service.dataaccess.customer.entity.CustomerEntity;
import com.training.food.ordering.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                //.username(customer.getUsername())
                //.firstName(customer.getFirstName())
                //.lastName(customer.getLastName())
                .build();
    }
}
