package com.training.food.order.service.domain.ports.output.repository;

import com.training.food.ordering.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
