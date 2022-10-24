package com.training.food.ordering.restaurant.service.domain.ports.input.message.listener;

import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {

    void approveOrder(RestaurantApprovalRequest request);

}
