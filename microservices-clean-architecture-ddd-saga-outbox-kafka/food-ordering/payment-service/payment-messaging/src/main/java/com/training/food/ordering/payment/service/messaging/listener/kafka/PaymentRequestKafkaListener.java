package com.training.food.ordering.payment.service.messaging.listener.kafka;

import com.training.food.ordering.kafka.consumer.KafkaConsumer;
import com.training.food.ordering.kafka.order.avro.model.PaymentOrderStatus;
import com.training.food.ordering.kafka.order.avro.model.PaymentRequestAvroModel;
import com.training.food.ordering.payment.service.domain.exception.PaymentApplicationServiceException;
import com.training.food.ordering.payment.service.domain.exception.PaymentNotFoundException;
import com.training.food.ordering.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.training.food.ordering.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
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
            try {
                if(PaymentOrderStatus.PENDING == avroModel.getPaymentOrderStatus()) {
                    log.info("Processing payment for order id: {}", avroModel.getOrderId());
                    listener.completePayment(mapper.paymentRequestAvroModelToPaymentRequest(avroModel));
                }else if(PaymentOrderStatus.CANCELLED == avroModel.getPaymentOrderStatus()) {
                    log.info("Canceling payment for order id: {}", avroModel.getOrderId());
                    listener.cancelPayment(mapper.paymentRequestAvroModelToPaymentRequest(avroModel));
                }
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if(sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    //NO-OP for unique constraint exception check
                    //payment_order_outbox_saga_id_payment_status_outbox_status index
                    log.error("Caught unique constraint exception with sql state: {}" +
                            " in PaymentRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(),
                            avroModel.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                //NO-OP for not found exc.
                log.error("No payment found for order id: {}", avroModel.getOrderId());
            }
        });

    }
}
