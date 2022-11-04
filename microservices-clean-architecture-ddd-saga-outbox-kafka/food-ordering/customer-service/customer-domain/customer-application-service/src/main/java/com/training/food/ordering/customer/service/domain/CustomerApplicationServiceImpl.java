package com.training.food.ordering.customer.service.domain;

import com.training.food.ordering.customer.service.domain.create.CreateCustomerCommand;
import com.training.food.ordering.customer.service.domain.create.CreateCustomerResponse;
import com.training.food.ordering.customer.service.domain.mapper.CustomerMapper;
import com.training.food.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.training.food.ordering.customer.service.domain.ports.output.kafka.publisher.CustomerMessagePublisher;
import com.training.food.ordering.customer.sesrvice.domain.event.CustomerCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CustomerApplicationServiceImpl implements CustomerApplicationService {

    private final CustomerCreateCommandHandler handler;
    private final CustomerMapper mapper;
    private final CustomerMessagePublisher publisher;

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand command) {
        CustomerCreatedEvent event = handler.createCustomer(command);
        publisher.publish(event);
        return mapper.customerToCreateCustomerResponse(event.getCustomer(),
                "Customer saved successfully!");
    }
}
