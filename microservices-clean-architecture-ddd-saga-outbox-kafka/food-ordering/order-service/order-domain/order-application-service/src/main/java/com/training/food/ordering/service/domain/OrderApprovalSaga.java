package com.training.food.ordering.service.domain;

import com.training.food.order.outbox.OutboxStatus;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.event.OrderCancelledEvent;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.saga.SagaStep;
import com.training.food.ordering.service.domain.dto.message.RestaurantApprovalResponse;
import com.training.food.ordering.service.domain.mapper.OrderDataMapper;
import com.training.food.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.training.food.ordering.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.training.food.ordering.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.training.food.ordering.order.service.domain.OrderDomainServiceImpl.UTC;

@Slf4j
@Component
@AllArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService service;
    private final OrderSagaHelper sagaHelper;

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    @Override
    public void process(RestaurantApprovalResponse response) {

        Optional<OrderApprovalOutboxMessage> optionalMessage =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                    UUID.fromString(response.getSagaId()),
                    SagaStatus.PROCESSING);

        if(optionalMessage.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!",
                    response.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage message = optionalMessage.get();

        Order order = approveOrder(response);

        SagaStatus sagaStatus = sagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(message,
                order.getOrderStatus(),
                sagaStatus));

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(response.getSagaId(),
                order.getOrderStatus(),
                sagaStatus));

        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaId,
                                                                     OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {



        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.PROCESSING);
        if (orderPaymentOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Payment outbox message cannot be found in " +
                    SagaStatus.PROCESSING.name() + " state");
        }
        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();
        orderPaymentOutboxMessage.setProcessAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage message,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {

        message.setProcessAt(ZonedDateTime.now(ZoneId.of("UTC")));
        message.setOrderStatus(orderStatus);
        message.setSagaStatus(sagaStatus);
        return message;
    }

    private Order approveOrder(RestaurantApprovalResponse response) {
        log.info("Approving order with id: {}", response.getOrderId());
        Order order = sagaHelper.findOrder(response.getOrderId());
        service.approveOrder(order);
        sagaHelper.saveOrder(order);
        return order;
    }

    @Transactional
    @Override
    public void rollback(RestaurantApprovalResponse response) {

        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(response.getSagaId()),
                        SagaStatus.PROCESSING);

        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!",
                    response.getSagaId());
            return;
        }

        OrderApprovalOutboxMessage message = orderApprovalOutboxMessageResponse.get();

        OrderCancelledEvent event = rollbackOrder(response);

        SagaStatus sagaStatus = sagaHelper.orderStatusToSagaStatus(event.getOrder().getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(message,
                event.getOrder().getOrderStatus(),
                sagaStatus));

        paymentOutboxHelper.savePaymentOutboxMessage(orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(event),
                event.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(response.getSagaId()));

        log.info("Order with id: {} is canceling", event.getOrder().getId().getValue());
    }

    private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = sagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domainEvent = service.cancelOrderPayment(order,
                restaurantApprovalResponse.getFailureMessages());
        sagaHelper.saveOrder(order);
        return domainEvent;
    }
}
