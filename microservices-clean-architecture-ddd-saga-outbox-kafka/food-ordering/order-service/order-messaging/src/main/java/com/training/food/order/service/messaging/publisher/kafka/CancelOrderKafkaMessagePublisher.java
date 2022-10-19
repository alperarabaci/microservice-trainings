package com.training.food.order.service.messaging.publisher.kafka;

import com.training.food.order.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.order.kafka.producer.service.KafkaProducer;
import com.training.food.order.service.domain.config.OrderServiceConfigData;
import com.training.food.order.service.domain.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.training.food.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.training.food.ordering.order.service.domain.event.OrderCancelledEvent;
import com.training.food.ordering.order.service.domain.event.OrderCreateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@AllArgsConstructor
public class CancelOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;

    private final OrderKafkaMessageHelper kafkaHelper;

    @Override
    public void publish(OrderCancelledEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId() .getValue().toString();
        try {
            log.info("Received OrderCreatedEvent for order id: {>", orderId);
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCanceledEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaHelper. getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
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
