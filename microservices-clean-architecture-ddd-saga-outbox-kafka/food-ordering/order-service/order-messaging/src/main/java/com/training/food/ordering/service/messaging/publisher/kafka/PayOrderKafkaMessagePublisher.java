package com.training.food.ordering.service.messaging.publisher.kafka;

import com.training.food.ordering.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.training.food.ordering.kafka.producer.service.KafkaProducer;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import com.training.food.ordering.service.domain.config.OrderServiceConfigData;
import com.training.food.ordering.service.domain.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.training.food.ordering.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;

    private final OrderKafkaMessageHelper kafkaHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        try {
            RestaurantApprovalRequestAvroModel avroModel = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    avroModel,
                    kafkaHelper.getKafkaCallback(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            avroModel,
                            orderId,
                            "RestaurantApprovalRequestAvroModel"
                            )
            );
            log.info("RestaurantApprovalRequestAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel" +
                            " to kafka with order id: {} and saga id: {}, error: {}",
                    orderId, e.getMessage());
        }
    }
}
