package com.training.food.ordering.service.messaging.listener.kafka;

import com.training.food.ordering.kafka.consumer.KafkaConsumer;
import com.training.food.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import com.training.food.ordering.kafka.order.avro.model.PaymentStatus;
import com.training.food.ordering.service.domain.dto.message.PaymentResponse;
import com.training.food.ordering.service.domain.ports.input.message.listener.payment.PaymentMessageResponseListener;
import com.training.food.ordering.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentMessageResponseListener listener;
    private final OrderMessagingDataMapper mapper;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment responses received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(avroModel -> {
            PaymentResponse response;
            if (PaymentStatus.COMPLETED == avroModel.getPaymentStatus()) {
                log.info("Processing successful payment for order id: {}", avroModel.getOrderId());
                response = mapper.paymentResponseAvroModelToPaymentResponse(avroModel);
                listener.paymentCompleted(response);
            } else if (PaymentStatus.CANCELLED == avroModel.getPaymentStatus() ||
                PaymentStatus.FAILED == avroModel.getPaymentStatus()) {
                log.info("Processing unsuccessful payment for order id: {}", avroModel.getOrderId());
                listener.paymentCancelled(mapper
                    .paymentResponseAvroModelToPaymentResponse(avroModel));
            }
        });
    }
}
