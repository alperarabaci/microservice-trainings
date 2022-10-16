package com.training.food.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class OrderAddressDto {
    @Max(value=50)
    @NotNull
    private final String street;
    @Max(value=10)
    @NotNull
    private final String postalCode;
    @Max(value=50)
    @NotNull
    private final String city;
}
