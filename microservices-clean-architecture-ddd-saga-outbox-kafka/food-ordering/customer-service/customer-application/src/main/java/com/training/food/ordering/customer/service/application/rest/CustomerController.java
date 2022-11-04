package com.training.food.ordering.customer.service.application.rest;

import com.training.food.ordering.customer.service.domain.create.CreateCustomerCommand;
import com.training.food.ordering.customer.service.domain.create.CreateCustomerResponse;
import com.training.food.ordering.customer.service.domain.ports.input.service.CustomerApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping( value = "/customers", produces = "application/vnd.api.v1+json")
@AllArgsConstructor
public class CustomerController {

    private final CustomerApplicationService service;

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerCommand command) {
        log.info("Creating customer with username: {}", command.getUsername());
        CreateCustomerResponse response = service.createCustomer(command);
        return ResponseEntity.ok(response);
    }

}
