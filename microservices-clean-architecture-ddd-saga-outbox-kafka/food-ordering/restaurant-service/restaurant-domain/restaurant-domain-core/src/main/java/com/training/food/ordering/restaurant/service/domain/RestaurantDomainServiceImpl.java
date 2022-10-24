package com.training.food.ordering.restaurant.service.domain;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.domain.valueobject.OrderApprovalStatus;
import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import com.training.food.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {
    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovedEvent> approvePublisher,
                                            DomainEventPublisher<OrderRejectedEvent> rejectPublisher) {

        restaurant.validateOrder(failureMessages);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());

        if(failureMessages.isEmpty()) {
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVAL);
            return new OrderApprovedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of("UTC")),
                    approvePublisher);
        } else {
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of("UTC")),
                    rejectPublisher);
        }

    }
}
