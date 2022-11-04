package com.training.food.ordering.customer.service.domain.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public record CreateCustomerResponse(@NotNull UUID customerId, @NotNull String message) {

}
