package com.training.food.ordering.restaurant.service.dataaccess.restaurant.adapter;

import com.training.food.ordering.dataaccess.restaurant.entity.RestaurantEntity;
import com.training.food.ordering.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.training.food.ordering.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository,
                                    RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurant(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }

}
