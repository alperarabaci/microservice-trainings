package com.training.food.ordering.customer.service.dataaccess.customer.adapeter;

import com.training.food.ordering.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.training.food.ordering.customer.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.training.food.ordering.customer.service.dataaccess.customer.repositor.CustomerJpaRepository;
import com.training.food.ordering.customer.service.domain.ports.output.repository.CustomerRepository;
import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository repository;
    private final CustomerDataAccessMapper mapper;

    @Override
    public Customer createCustomer(Customer customer) {
        CustomerEntity entity = mapper.customerToCustomerEntity(customer);
        CustomerEntity savedEntity = repository.save(entity);
        return mapper.customerEntityToCustomer(savedEntity);
    }


}
