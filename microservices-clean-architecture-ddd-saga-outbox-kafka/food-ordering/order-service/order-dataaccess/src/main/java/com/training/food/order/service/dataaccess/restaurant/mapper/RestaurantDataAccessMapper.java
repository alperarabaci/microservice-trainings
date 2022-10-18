package com.training.food.order.service.dataaccess.restaurant.mapper;

import com.training.food.order.domain.valueobject.Money;
import com.training.food.order.domain.valueobject.ProductId;
import com.training.food.order.domain.valueobject.RestaurantId;
import com.training.food.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.training.food.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.training.food.ordering.order.service.domain.entity.Product;
import com.training.food.ordering.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        //mappers only job is mapping. should be validated before to prevent throwing exception.
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
