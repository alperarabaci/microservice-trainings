package com.training.food.ordering.service.domain;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.domain.valueobject.OrderStatus;
import com.training.food.ordering.domain.valueobject.PaymentStatus;
import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.order.service.domain.exception.OrderNotFoundException;
import com.training.food.ordering.outbox.OutboxStatus;
import com.training.food.ordering.saga.SagaStatus;
import com.training.food.ordering.saga.SagaStep;
import com.training.food.ordering.service.domain.dto.message.PaymentResponse;
import com.training.food.ordering.service.domain.mapper.OrderDataMapper;
import com.training.food.ordering.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.training.food.ordering.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.training.food.ordering.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.training.food.ordering.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.training.food.ordering.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService service;

    private final OrderRepository orderRepository;

    private final OrderSagaHelper sagaHelper;

    private final PaymentOutboxHelper paymentOutboxHelper;

    private final ApprovalOutboxHelper approvalOutboxHelper;

    private final OrderDataMapper orderDataMapper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> outboxMessageResponse =
                 paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(paymentResponse.getSagaId()),
                SagaStatus.STARTED
        );

        if(outboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = outboxMessageResponse.get();

        OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);

        SagaStatus sagaStatus = sagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

        OrderPaymentOutboxMessage outboxMessage = getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus);
        paymentOutboxHelper.save(outboxMessage);

        OrderApprovalEventPayload payload = orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent);
        approvalOutboxHelper.saveApprovalOutboxMessage(payload,
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId()));
        
        log.info("Order with id: {} is paid", domainEvent.getOrder().getId().getValue());
    }

    private Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                                                                     OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessAt(ZonedDateTime.now(ZoneId.of("UTC")));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return orderPaymentOutboxMessage;
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessageResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(paymentResponse.getSagaId()),
                getCurrencSagaStatus(paymentResponse.getPaymentStatus()));


        if(orderPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!",
                    paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessageResponse.get();

        Order order = rollbackPaymentForOrder(paymentResponse);

        SagaStatus sagaStatus = sagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
                order.getOrderStatus(),
                sagaStatus));

        if(paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(paymentResponse.getSagaId(),
                    order.getOrderStatus(),
                    sagaStatus));
        }
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        Optional<OrderApprovalOutboxMessage> response =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                    UUID.fromString(sagaId),
                    SagaStatus.COMPENSATING);

        if(response.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in" +
                    SagaStatus.COMPENSATING.name() + " status!");
        }

        OrderApprovalOutboxMessage message = response.get();
        message.setProcessAt(ZonedDateTime.now(ZoneId.of("UTC")));
        message.setOrderStatus(orderStatus);
        message.setSagaStatus(sagaStatus);

        return message;
    }

    private SagaStatus[] getCurrencSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[] {SagaStatus.STARTED};
            case CANCELLED -> new SagaStatus[] {SagaStatus.PROCESSING};
            case FAILED -> new SagaStatus[] {SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    public OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id: {}", paymentResponse.getOrderId());
        Order order = findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domainEvent = service.payOrder(order);
        orderRepository.save(order);

        return domainEvent;
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Canceling order with id: {}", paymentResponse.getOrderId());
        Order order = sagaHelper.findOrder(paymentResponse.getOrderId());
        service.cancelOrder(order, paymentResponse.getFailureMessages());
        sagaHelper.saveOrder(order);
        return order;
    }
}
