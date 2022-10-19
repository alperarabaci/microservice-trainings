package com.training.food.order.service.messaging.publisher.kafka;

import com.training.food.order.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String responseTopicName, T requestAvroModel, String orderId, String avroModelName) {

        return new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {}" +
                                "message {} to topic {}", avroModelName, requestAvroModel.toString(),
                        responseTopicName, ex);
            }

            @Override
            public void onSuccess(SendResult<String, PaymentRequestAvroModel> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successfull response from Kfka for order id: {}"
                                + "Topic: {} Partition: {} Offset: {} ",
                        orderId,
                        metadata.topic(),
                        metadata.offset(),
                        metadata.timestamp());
            }
        };
    }

}
