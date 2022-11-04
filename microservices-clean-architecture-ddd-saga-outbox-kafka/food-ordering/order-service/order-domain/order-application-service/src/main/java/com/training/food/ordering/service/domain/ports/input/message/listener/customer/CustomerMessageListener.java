package com.training.food.ordering.service.domain.ports.input.message.listener.customer;

import com.training.food.ordering.service.domain.dto.message.CustomerModel;

public interface CustomerMessageListener {

    void customerCreated(CustomerModel customerModel);

}
