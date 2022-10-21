package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.payment.service.domain.dto.PaymentRequest;
import com.training.food.ordering.payment.service.domain.event.PaymentCanceledEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentEvent;
import com.training.food.ordering.payment.service.domain.event.PaymentFailedEvent;
import com.training.food.ordering.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentCanceledMessagePublisher;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.training.food.ordering.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    /**
     * Refactoring: Replaced with => event.fire();
     */
    /*
    private final PaymentCompletedMessagePublisher completedMessagePublisher;
    private final PaymentCanceledMessagePublisher canceledMessagePublisher;
    private final PaymentFailedMessagePublisher failedMessagePublisher;
    */

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent event = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(event);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent event = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(event);
    }

    private void fireEvent(PaymentEvent event) {
        log.info("Publishing payment event with payment id: {} and order id: {}",
                event.getPayment().getId(),
                event.getPayment().getOrderId().getValue());

        event.fire();

        /**
         * Refactoring: Replaced with => event.fire();
         */
        /*
        if (event instanceof PaymentCompletedEvent) {
            completedMessagePublisher.publish((PaymentCompletedEvent) event);
        } else if (event instanceof PaymentFailedEvent) {
            failedMessagePublisher.publish((PaymentFailedEvent)) event);
        } else if (event instanceof PaymentCanceledEvent) {
            canceledMessagePublisher.publish((PaymentCompletedEvent) event);
        }
        */
    }


}
