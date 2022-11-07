package com.training.food.ordering.customer.service;

import com.training.food.ordering.customer.sesrvice.domain.CustomerDomainService;
import com.training.food.ordering.customer.sesrvice.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }

}
