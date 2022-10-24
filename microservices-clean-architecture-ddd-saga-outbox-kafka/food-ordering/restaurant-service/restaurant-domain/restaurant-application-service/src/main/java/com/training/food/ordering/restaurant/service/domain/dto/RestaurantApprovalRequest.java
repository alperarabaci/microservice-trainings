package com.training.food.ordering.restaurant.service.domain.dto;

import com.training.food.ordering.domain.valueobject.RestaurantOrderStatus;
import com.training.food.ordering.restaurant.service.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {

    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus orderStatus;
    private List<Product> products;
    private BigDecimal price;
    private Instant createdAt;


}
