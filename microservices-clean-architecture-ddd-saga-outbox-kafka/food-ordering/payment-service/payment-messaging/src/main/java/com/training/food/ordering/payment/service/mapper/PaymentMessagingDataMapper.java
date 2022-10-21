package com.training.food.ordering.payment.service.mapper;

import com.training.food.ordering.domain.valueobject.PaymentOrderStatus;
import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import com.training.food.ordering.kafka.order.avro.model.PaymentStatus;
import com.training.food.ordering.payment.service.domain.dto.PaymentRequest;
import com.training.food.ordering.payment.service.domain.entity.Payment;
import com.training.food.ordering.payment.service.domain.event.PaymentCanceledEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {


    public PaymentResponseAvroModel
    paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent event) {
        Payment payment = event.getPayment();

        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(payment.getId().toString())
                .setCustomerId(payment.getCustomerId().toString())
                .setOrderId(payment.getOrderId().toString())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(payment.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel
    paymentCanceledEventToPaymentResponseAvroModel(PaymentCanceledEvent event) {
        Payment payment = event.getPayment();

        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(payment.getId().toString())
                .setCustomerId(payment.getCustomerId().toString())
                .setOrderId(payment.getOrderId().toString())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(payment.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentResponseAvroModel
    paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent event) {
        Payment payment = event.getPayment();

        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(payment.getId().toString())
                .setCustomerId(payment.getCustomerId().toString())
                .setOrderId(payment.getOrderId().toString())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(payment.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus().name()))
                .setFailureMessages(event.getFailureMessages())
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaId(paymentRequestAvroModel.getSagaId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }

}
