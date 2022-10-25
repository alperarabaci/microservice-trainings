package com.training.food.ordering.restaurant.service.messaging.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.training.food.ordering.kafka.producer.KafkaMessageHelper;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.training.food.ordering.restaurant.service.domain.event.OrderRejectedEvent;
import com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.training.food.ordering.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@AllArgsConstructor
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

    private final RestaurantMessagingDataMapper mapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData configData;
    private final KafkaMessageHelper messageHelper;

    @Override
    public void publish(OrderRejectedEvent event) {
        String orderId = event.getOrderApproval().getOrderId().getValue().toString();

        log.info("Received OrderRejectedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel model = mapper.orderRejectedEventTORestaurantApprovalResponseAvroModel(event);
            ListenableFutureCallback<SendResult<String, RestaurantApprovalResponseAvroModel>> kafkaCallback = messageHelper.getKafkaCallback(
                    configData.getRestaurantApprovalResponseTopicName(),
                    model,
                    orderId,
                    "RestaurantApprovalResponseAvroModel");

            kafkaProducer.send(configData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    model,
                    kafkaCallback);

            log.info("RestaurantApprovalResponseAvroModel sent to kafka at {}", System.nanoTime());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
