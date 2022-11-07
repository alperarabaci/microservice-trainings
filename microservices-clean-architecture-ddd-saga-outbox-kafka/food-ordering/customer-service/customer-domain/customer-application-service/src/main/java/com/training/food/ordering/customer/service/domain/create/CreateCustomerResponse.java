package com.training.food.ordering.customer.service.domain.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerResponse {

    @NotNull UUID customerId;
    @NotNull String message;

}
