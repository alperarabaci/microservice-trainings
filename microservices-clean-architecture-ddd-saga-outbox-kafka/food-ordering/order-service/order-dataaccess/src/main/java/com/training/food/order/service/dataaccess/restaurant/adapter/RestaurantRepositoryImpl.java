package com.training.food.order.service.dataaccess.restaurant.adapter;

import com.training.food.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.training.food.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.training.food.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.training.food.order.service.domain.ports.output.repository.RestaurantRepository;
import com.training.food.ordering.order.service.domain.entity.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
