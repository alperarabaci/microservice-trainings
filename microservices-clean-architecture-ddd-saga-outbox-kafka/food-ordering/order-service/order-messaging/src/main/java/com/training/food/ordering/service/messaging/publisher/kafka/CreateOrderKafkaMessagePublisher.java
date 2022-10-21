package com.training.food.ordering.service.messaging.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.service.domain.config.OrderServiceConfigData;
import com.training.food.ordering.service.domain.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.training.food.ordering.service.messaging.mapper.OrderMessagingDataMapper;
import com.training.food.ordering.order.service.domain.event.OrderCreateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;


    private final OrderKafkaMessageHelper kafkaHelper;

    @Override
    public void publish(OrderCreateEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId() .getValue().toString();
        try {
            log.info("Received OrderCreatedEvent for order id: {>", orderId);
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaHelper.getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel")
            );
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel" +
                            " to kafka with order id: {} and saga id: {}, error: {}",
                    orderId, e.getMessage());
        }
    }

}
