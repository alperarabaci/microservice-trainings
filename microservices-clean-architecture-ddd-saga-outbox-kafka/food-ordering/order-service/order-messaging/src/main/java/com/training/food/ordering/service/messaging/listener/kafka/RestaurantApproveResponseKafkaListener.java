package com.training.food.ordering.service.messaging.listener.kafka;

import com.training.food.ordering.kafka.consumer.KafkaConsumer;
import com.training.food.ordering.kafka.order.avro.model.OrderApprovalStatus;
import com.training.food.ordering.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
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
public class RestaurantApproveResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener listener;
    private final OrderMessagingDataMapper mapper;

    @Override
    @KafkaListener(id = "{kafka-consumer-config.restaurant-consumer-group-id}",
            topics = "order-service.restaurant-approval-response-topic-name")
    public void receive(@Payload  List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        messages.forEach(avroModel -> {
            if(avroModel.getOrderApprovalStatus() == OrderApprovalStatus.APPROVED) {
                log.info("Processing approved order for order id: {}",
                        avroModel.getOrderId());
                RestaurantApprovalResponse response = mapper.approvalResponseAvroModelToApprovalResponse(avroModel);
                listener.orderApproved(response);
            } else if(avroModel.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED) {
                log.info("Processing rejected order for order id: {}, with failure messages: {}",
                        avroModel.getOrderId(),
                        String.join(Order.FAILURE_MESSAGE_DELIMITER, avroModel.getFailureMessages()));
                RestaurantApprovalResponse response = mapper.approvalResponseAvroModelToApprovalResponse(avroModel);
                listener.orderRejected(response);
            }

        });

    }
}
