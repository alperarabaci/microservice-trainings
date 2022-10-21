package com.training.food.ordering.service.domain;

import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Component
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {


    @Override
    public void orderApproved(RestaurantApprovalResponse response) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {

    }
}
