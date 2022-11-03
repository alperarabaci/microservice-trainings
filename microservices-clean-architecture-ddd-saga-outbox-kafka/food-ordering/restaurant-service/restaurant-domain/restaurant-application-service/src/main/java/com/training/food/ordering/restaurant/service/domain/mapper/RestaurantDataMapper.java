package com.training.food.ordering.restaurant.service.domain.mapper;

import com.training.food.ordering.domain.valueobject.Money;
import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.domain.valueobject.RestaurantId;
import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.training.food.ordering.restaurant.service.domain.entity.OrderDetail;
import com.training.food.ordering.restaurant.service.domain.entity.Product;
import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import com.training.food.ordering.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest request) {
        RestaurantId restaurantId = new RestaurantId(UUID.fromString(request.getRestaurantId()));
        OrderId orderId = new OrderId(UUID.fromString(request.getOrderId()));

        OrderDetail orderDetail = OrderDetail.builder().id(orderId)
                .products(createProducts(request))
                .totalAmount(new Money(request.getPrice()))
                .orderStatus(OrderStatus.valueOf(request.getOrderStatus().name()))
                .build();

        return Restaurant.builder()
                .id(restaurantId)
                .orderDetail(orderDetail)
                .build();
    }

    private static List<Product> createProducts(RestaurantApprovalRequest request) {
        return request.getProducts()
                .stream()
                .map(RestaurantDataMapper::createProduct)
                .collect(Collectors.toList());
    }

    private static Product createProduct(Product product) {
        return Product.builder()
                .id(product.getId())
                .quantity(product.getQuantity())
                .build();
    }

    public OrderEventPayload
    orderApprovalEventToOrderEventPayload(OrderApprovalEvent orderApprovalEvent) {
        return OrderEventPayload.builder()
                .orderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
                .restaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
                .orderApprovalStatus(orderApprovalEvent.getOrderApproval().getApprovalStatus().name())
                .createdAt(orderApprovalEvent.getCreatedAt())
                .failureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }

}
