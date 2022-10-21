package com.training.food.ordering.service.messaging.mapper;

import com.training.food.order.kafka.order.avro.model.*;
import com.training.food.ordering.domain.valueobject.OrderApprovalStatus;
import com.training.food.ordering.domain.valueobject.PaymentStatus;
import com.training.food.ordering.kafka.order.avro.model.*;
import com.training.food.ordering.service.domain.dto.message.PaymentResponse;
import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
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
                    .setRestaurantOrderStatus(RestaurantOrderStatus
                                                      .valueOf(order.getOrderStatus().name()))
                    .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem. getProduct().getId().getValue().toString())
                    .setQuantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList()))
                    .setPrice(order.getPrice().getAmount())
                    .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                    .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                    .build() ;
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel
                                paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .orderId (paymentResponseAvroModel. getOrderId())
                .price (paymentResponseAvroModel. getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(
                        paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                . build () ;
    }

    public RestaurantApprovalResponse approvalResponseAvroModelToApprovalResponse(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(restaurantApprovalResponseAvroModel.getId())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(
                        restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
    }
}
