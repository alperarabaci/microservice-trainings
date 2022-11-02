package com.training.food.ordering.service.messaging.publisher.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.kafka.order.avro.model.PaymentOrderStatus;
import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.kafka.producer.KafkaMessageHelper;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.service.domain.config.OrderServiceConfigData;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.training.food.ordering.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.training.food.ordering.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiConsumer;

@Slf4j
@Component
@AllArgsConstructor
public class OrderPaymentEventKafkaPublisher implements PaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData configData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final ObjectMapper objectMapper;


    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        OrderPaymentEventPayload orderPaymentEventPayload =
                getOrderPaymentEventPayload(orderPaymentOutboxMessage.getPayload());

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info("Received OrderPaymentOutboxMessage for order id: {} and saga id: {}",
                orderPaymentEventPayload.getOrderId(),
                sagaId);

        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            kafkaProducer.send(configData.getPaymentRequestTopicName(),
                    sagaId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(configData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderPaymentOutboxMessage,
                            outboxCallback,
                            orderPaymentEventPayload.getOrderId(),
                            "PaymentRequestAvroModel"));

            log.info("OrderPaymentEventPayload sent to Kafka for order id: {} and saga id: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);
        } catch (Exception e) {
            log.error("Error while sending OrderPaymentEventPayload" +
                            " to kafka with order id: {} and saga id: {}, error: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());
        }
    }

    private OrderPaymentEventPayload getOrderPaymentEventPayload(String payload) {
        try {
            return objectMapper.readValue(payload, OrderPaymentEventPayload.class);
        } catch (JsonProcessingException e) {
            log.error("Could not read OrderPaymentEventPayload object!", e);
            throw new OrderDomainException("Could not read OrderPaymentEventPayload object!", e);
        }
    }
}
