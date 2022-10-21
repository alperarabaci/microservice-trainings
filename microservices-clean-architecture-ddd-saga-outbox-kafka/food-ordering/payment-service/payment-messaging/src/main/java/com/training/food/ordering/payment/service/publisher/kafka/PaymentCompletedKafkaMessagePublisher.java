package com.training.food.ordering.payment.service.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.PaymentResponseAvroModel;
import com.training.food.ordering.kafka.producer.KafkaMessageHelper;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.payment.service.domain.config.PaymentServiceConfigData;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.training.food.ordering.payment.service.mapper.PaymentMessagingDataMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentCompletedKafkaMessagePublisher implements PaymentCompletedMessagePublisher {

    private final PaymentMessagingDataMapper mapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentServiceConfigData configData;

    private final KafkaMessageHelper kafkaHelper;

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

        log.info("Received PaymentCompletedEvent for order id: {}", orderId);

        try {
            PaymentResponseAvroModel model = mapper.paymentCompletedEventToPaymentResponseAvroModel(domainEvent);

            ListenableFutureCallback<SendResult<String, PaymentResponseAvroModel>> kafkaCallback = kafkaHelper.getKafkaCallback(configData.getPaymentResponseTopicName(),
                    model,
                    orderId,
                    "PaymentResponseAvroModel");

            //TODO will be added saga and outbox pattern.
            kafkaProducer.send(configData.getPaymentRequestTopicName(),
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
