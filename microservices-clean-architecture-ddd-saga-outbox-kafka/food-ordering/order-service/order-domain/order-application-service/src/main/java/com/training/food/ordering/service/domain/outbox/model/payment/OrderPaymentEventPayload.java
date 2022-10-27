package com.training.food.ordering.service.domain.outbox.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.saga.SagaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderPaymentEventPayload {

    @JsonProperty
    private String orderId;
    @JsonProperty
    private String customerId;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private ZonedDateTime createdAt;
    @JsonProperty
    private String paymentOrderStatus;

}

