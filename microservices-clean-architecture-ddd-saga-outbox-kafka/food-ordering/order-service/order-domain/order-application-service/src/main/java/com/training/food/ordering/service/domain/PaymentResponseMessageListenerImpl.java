package com.training.food.ordering.service.domain;

import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import com.training.food.ordering.service.domain.dto.message.PaymentResponse;
import com.training.food.ordering.service.domain.ports.input.message.listener.payment.PaymentMessageResponseListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static com.training.food.ordering.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Validated
@Component
@Slf4j
@AllArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentMessageResponseListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent domainEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getOrderId());
        domainEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
    }
}
