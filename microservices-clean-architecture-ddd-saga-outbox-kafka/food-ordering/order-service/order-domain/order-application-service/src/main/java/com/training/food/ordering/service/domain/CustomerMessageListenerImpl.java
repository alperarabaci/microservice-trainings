package com.training.food.ordering.service.domain;

import com.training.food.ordering.order.service.domain.entity.Customer;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.service.domain.dto.message.CustomerModel;
import com.training.food.ordering.service.domain.mapper.OrderDataMapper;
import com.training.food.ordering.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.training.food.ordering.service.domain.ports.output.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository repository;
    private final OrderDataMapper mapper;

    @Override
    public void customerCreated(CustomerModel model) {
        Customer customer = repository.save(mapper.customerModelToCustomer(model));

        if(customer == null) {
            log.error("Customer could not be created in order database with id: {}", model.getId());
            throw new OrderDomainException("Customer could not be created in order to database with id:"+
             customer.getId());
        }
        log.info("Customer is created in order database with id: {}", customer.getId());
    }
}
