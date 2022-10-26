package com.training.food.ordering.payment.service.messaging.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import com.training.food.ordering.kafka.producer.KafkaMessageHelper;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.payment.service.domain.config.PaymentServiceConfigData;
import com.training.food.ordering.payment.service.domain.event.PaymentFailedEvent;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.training.food.ordering.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentFailedKafkaMessagePublisher implements PaymentFailedMessagePublisher {

    private final PaymentMessagingDataMapper mapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentServiceConfigData configData;

    private final KafkaMessageHelper kafkaHelper;

    @Override
    public void publish(PaymentFailedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

        log.info("Received PaymentFailedEvent for order id: {}", orderId);

        try {
            PaymentResponseAvroModel model = mapper.paymentFailedEventToPaymentResponseAvroModel(domainEvent);

            ListenableFutureCallback<SendResult<String, PaymentResponseAvroModel>> kafkaCallback = kafkaHelper.getKafkaCallback(configData.getPaymentResponseTopicName(),
                    model,
                    orderId,
                    "PaymentResponseAvroModel");

            //TODO will be added saga and outbox pattern.
            kafkaProducer.send(configData.getPaymentResponseTopicName(),
                    orderId,
                    model,
                    kafkaCallback);

            log.info("Error while sending PaymentResponseAvroModel message" +
                    "to kafka with order id: {}, error {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message" +
                    " to kafka with order id: {}, error {}", orderId, e.getMessage());
        }
    }


}
