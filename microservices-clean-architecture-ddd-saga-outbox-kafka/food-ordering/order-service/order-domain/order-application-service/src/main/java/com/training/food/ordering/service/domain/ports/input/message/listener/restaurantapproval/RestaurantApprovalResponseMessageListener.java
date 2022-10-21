package com.training.food.ordering.service.domain.ports.input.message.listener.restaurantapproval;

import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(RestaurantApprovalResponse response);

    void orderRejected(RestaurantApprovalResponse response);
}
