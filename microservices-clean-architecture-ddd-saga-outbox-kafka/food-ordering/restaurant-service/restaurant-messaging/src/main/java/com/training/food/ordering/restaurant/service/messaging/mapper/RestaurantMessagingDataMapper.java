package com.training.food.ordering.restaurant.service.messaging.mapper;


import com.training.food.ordering.domain.valueobject.ProductId;
import com.training.food.ordering.domain.valueobject.RestaurantOrderStatus;
import com.training.food.ordering.kafka.order.avro.model.OrderApprovalStatus;
import com.training.food.ordering.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.training.food.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.training.food.ordering.restaurant.service.domain.entity.Product;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovedEvent;
import com.training.food.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import com.training.food.ordering.restaurant.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessagingDataMapper {

    public RestaurantApprovalRequest
    restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel
                                                                   restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .orderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts()
                        .stream().map(avroModel ->
                                Product.builder()
                                        .id(new ProductId(UUID.fromString(avroModel.getId())))
                                        .quantity(avroModel.getQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderApprovedEventTORestaurantApprovalResponseAvroModel(OrderApprovedEvent event) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(event.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(event.getOrderApproval().getRestaurantId().getValue().toString())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(event.getOrderApproval()
                        .getApprovalStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderRejectedEventTORestaurantApprovalResponseAvroModel(OrderRejectedEvent event) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(event.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(event.getOrderApproval().getRestaurantId().getValue().toString())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(event.getOrderApproval()
                        .getApprovalStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel
    orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId, OrderEventPayload orderEventPayload) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderEventPayload.getOrderId())
                .setRestaurantId(orderEventPayload.getRestaurantId())
                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
                .setFailureMessages(orderEventPayload.getFailureMessages())
                .build();
    }
}
