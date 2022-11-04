package com.training.food.ordering.customer.service.messaging.mapper;

import com.training.food.ordering.customer.sesrvice.domain.entity.Customer;
import com.training.food.ordering.customer.sesrvice.domain.event.CustomerCreatedEvent;
import com.training.food.ordering.kafka.order.avro.model.CustomerAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel paymentResponseAvroModelToPaymentResponse(CustomerCreatedEvent
                                                                               customerCreatedEvent) {

        Customer customer = customerCreatedEvent.getCustomer();
        return CustomerAvroModel.newBuilder()
                .setId(customer.getId().getValue().toString())
                .setUsername(customer.getUsername())
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .build();
    }
}
