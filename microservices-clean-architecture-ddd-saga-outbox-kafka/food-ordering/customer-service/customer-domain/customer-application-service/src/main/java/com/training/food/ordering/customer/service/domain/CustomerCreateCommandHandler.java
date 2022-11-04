package com.training.food.ordering.customer.service.domain;

import com.training.food.ordering.customer.service.domain.create.CreateCustomerCommand;
import com.training.food.ordering.customer.service.domain.mapper.CustomerMapper;
import com.training.food.ordering.customer.service.domain.ports.output.repository.CustomerRepository;
import com.training.food.ordering.customer.sesrvice.domain.CustomerDomainService;
import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import com.training.food.ordering.customer.sesrvice.domain.event.CustomerCreatedEvent;
import com.training.food.ordering.customer.sesrvice.domain.exception.CustomerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class CustomerCreateCommandHandler {

    private final CustomerDomainService domainService;
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand command) {
        Customer customer = mapper.createCustomerCommandToCustomer(command);
        CustomerCreatedEvent event = domainService.validateAndInitiateCustomer(customer);
        Customer savedCustomer = repository.createCustomer(customer);

        if(savedCustomer == null) {
            log.error("Could not save customer with id: {}", command.getCustomerId());
            throw new CustomerException("Could not save customer with id: {}" + command.getCustomerId());
        }

        return event;
    }
}
