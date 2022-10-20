package com.training.food.order.service.dataaccess.customer.adapter;

import com.training.food.order.service.dataaccess.customer.entity.CustomerEntity;
import com.training.food.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.training.food.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.training.food.order.service.domain.ports.output.repository.CustomerRepository;
import com.training.food.ordering.order.service.domain.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer));
        return customerDataAccessMapper.customerEntityToCustomer(entity);
    }
}
