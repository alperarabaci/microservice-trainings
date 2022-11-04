package com.training.food.ordering.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CustomerModel {

    private String id;
    private String username;
    private String firstName;
    private String lastName;

}
