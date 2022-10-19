package com.training.food.order.service.messaging.listener.kafka;

import com.training.food.order.kafka.consumer.KafkaConsumer;
import com.training.food.order.kafka.order.avro.model.PaymentResponseAvroModel;
import com.training.food.order.service.domain.dto.message.PaymentResponse;
import com.training.food.order.service.domain.ports.input.message.listener.payment.PaymentMessageResponseListener;
import com.training.food.order.service.messaging.mapper.OrderMessagingDataMapper;
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
            id = "{kafka-consumer-config.payment-consumer-group-id}",
            topics = "order-service.payment-response-topic")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment response received with keys: {}," +
                "partitions: {} and offset: {}");

        messages.forEach(avroModel -> {
            PaymentResponse response;
            if (com.training.food.order.kafka.order.avro.model.PaymentStatus.COMPLETED == avroModel.getPaymentStatus()) {
                log.info("Processing successful payment for order id: {}", avroModel.getOrderId());
                response = mapper.paymentResponseAvroModelToPaymentResponse(avroModel);
                listener.paymentCompleted(response);
            } else if (com.training.food.order.kafka.order.avro.model.PaymentStatus.COMPLETED == avroModel.getPaymentStatus()) {
                log.info("Processing successful payment for order id: {}", avroModel.getOrderId());
                response = mapper.paymentResponseAvroModelToPaymentResponse(avroModel);
                listener.paymentCompleted(response);
            }
        });
    }
}
