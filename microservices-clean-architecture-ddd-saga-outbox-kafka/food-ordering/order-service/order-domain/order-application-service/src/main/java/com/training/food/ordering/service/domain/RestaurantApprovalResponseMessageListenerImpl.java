package com.training.food.ordering.service.domain;

import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.training.food.ordering.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Validated
@Service
@AllArgsConstructor
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga saga;

    @Override
    public void orderApproved(RestaurantApprovalResponse response) {
        saga.process(response);
        log.info("Order is approved for order id: {}", response.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {
        saga.rollback(response);
        log.info("Order Approval Saga rollback operation is completed for order id: {} with failure messages: {}",
                response.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMITER, response.getFailureMessages()));
    }
}
