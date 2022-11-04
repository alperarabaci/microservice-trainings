package com.training.food.ordering.restaurant.service.domain;

import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;

import java.util.List;

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages);
}
