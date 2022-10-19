package com.training.food.order.service.messaging.mapper;

import com.training.food.order.kafka.order.avro.model.*;
import com.training.food.order.service.domain.dto.message.PaymentResponse;
import com.training.food.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.event.OrderCancelledEvent;
import com.training.food.ordering.order.service.domain.event.OrderCreateEvent;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreateEvent event) {
        Order order = event.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCanceledEventToPaymentRequestAvroModel(OrderCancelledEvent event) {
        Order order = event.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent){
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                    .setSagaId("")
                    .setOrderId(order.getId().getValue().toString())
                    .setRestaurantId(order.getRestaurantId().getValue().toString())
                    .setOrderId(order.getId().getValue().toString())
                    .setRestaurantOrderStatus(com.training.food.order.kafka.order.avro.model.RestaurantOrderStatus
                                                      .valueOf(order.getOrderStatus().name()))
                    .setProducts(order.getItems().stream().map(orderItem ->
                        com.training.food.order.kafka.order.avro.model.Product.newBuilder()
                                .setId(orderItem. getProduct().getId().getValue().toString())
                    .setQuantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                    .setPrice(order.getPrice().getAmount())
                    .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                    .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                    .build() ;
    }
}
