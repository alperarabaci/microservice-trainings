package com.training.food.ordering.customer.service.domain.ports.output.kafka.publisher;

import com.training.food.ordering.customer.sesrvice.domain.event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {
    void publish(CustomerCreatedEvent customerCreatedEvent);

}
