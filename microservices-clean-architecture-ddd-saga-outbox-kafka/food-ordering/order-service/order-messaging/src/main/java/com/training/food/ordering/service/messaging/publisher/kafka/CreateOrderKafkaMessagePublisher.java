package com.training.food.ordering.service.messaging.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.kafka.producer.KafkaMessageHelper;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import com.training.food.ordering.service.domain.config.OrderServiceConfigData;
import com.training.food.ordering.service.domain.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import com.training.food.ordering.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatePaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;


    private final KafkaMessageHelper kafkaHelper;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId() .getValue().toString();
        try {
            log.info("Received OrderCreatedEvent for order id: {}", orderId);
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaHelper.getKafkaCallback(orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel")
            );
            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }

}
