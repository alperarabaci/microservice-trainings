package com.training.food.ordering.restaurant.service.domain;

import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import com.training.food.ordering.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {

    private final RestaurantApprovalRequestHelper helper;

    @Override
    public void approveOrder(RestaurantApprovalRequest request) {
        helper.persistOrderApproval(request);
    }

}
