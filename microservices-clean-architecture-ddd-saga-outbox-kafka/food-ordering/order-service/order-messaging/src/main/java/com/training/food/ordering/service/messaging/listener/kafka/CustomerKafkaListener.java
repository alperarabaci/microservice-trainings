package com.training.food.ordering.service.messaging.listener.kafka;

import com.training.food.ordering.kafka.consumer.KafkaConsumer;
import com.training.food.ordering.kafka.order.avro.model.CustomerAvroModel;
import com.training.food.ordering.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
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
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerMessageListener listener;
    private final OrderMessagingDataMapper mapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}", topics = "${order-service.customer-topic-name}")
    public void receive(@Payload List<CustomerAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of customer create messages received with keys {}, partitions {} and offsets {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(customerAvroModel ->
                listener.customerCreated(mapper
                        .customerAvroModeltoCustomerModel(customerAvroModel)));
    }
}
