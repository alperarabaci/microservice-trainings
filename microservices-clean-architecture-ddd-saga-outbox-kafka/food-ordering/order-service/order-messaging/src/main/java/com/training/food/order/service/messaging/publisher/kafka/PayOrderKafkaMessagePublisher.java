package com.training.food.order.service.messaging.publisher.kafka;

import com.training.food.order.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.order.kafka.producer.service.KafkaProducer;
import com.training.food.order.service.domain.config.OrderServiceConfigData;
import com.training.food.order.service.domain.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.training.food.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderServiceConfigData orderServiceConfigData;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);
        
    }
}
