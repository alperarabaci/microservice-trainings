package com.training.food.ordering.payment.service.messaging.listener.kafka;

import com.training.food.ordering.kafka.consumer.KafkaConsumer;
import com.training.food.ordering.kafka.order.avro.model.PaymentOrderStatus;
import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.training.food.ordering.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener listener;
    private final PaymentMessagingDataMapper mapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
                    topics = "${payment-service.payment-request-topic-name}")
    public void receive(@Payload  List<PaymentRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        messages.forEach(avroModel -> {
            if(PaymentOrderStatus.PENDING == avroModel.getPaymentOrderStatus()) {
                log.info("Processing payment for order id: {}", avroModel.getOrderId());
                listener.completePayment(mapper.paymentRequestAvroModelToPaymentRequest(avroModel));
            }else if(PaymentOrderStatus.PENDING == avroModel.getPaymentOrderStatus()) {
                log.info("Canceling payment for order id: {}", avroModel.getOrderId());
                listener.cancelPayment(mapper.paymentCanceledEventToPaymentResponseAvroModel(avroModel));
            }
        });

    }
}
