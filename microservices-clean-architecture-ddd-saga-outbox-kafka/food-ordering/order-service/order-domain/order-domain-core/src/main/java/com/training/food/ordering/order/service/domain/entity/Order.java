package com.training.food.ordering.order.service.domain.entity;

import com.training.food.ordering.domain.entity.AggregateRoot;
import com.training.food.ordering.domain.valueobject.*;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import com.training.food.ordering.order.service.domain.valueobject.OrderItemId;
import com.training.food.ordering.order.service.domain.valueobject.StreetAddress;
import com.training.food.ordering.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;
    /**
     * No need to use this kind of dirty ugly things..
     * Save as list, serve as a list?
     */
    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemsPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + " is not equal to Order items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    private void validateItemsPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount()
                + " is not valid for product " + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if(price==null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if( orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }
    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId =  new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for(OrderItem item: items) {
            item.initiliazeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public void pay() {
        if(orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessage) {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELING;
        updateFailureMessages(failureMessage);
    }

    public void cancel(List<String> failureMessage) {
        if(!(orderStatus != OrderStatus.CANCELING || orderStatus != OrderStatus.PAID)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessage);
    }

    private void updateFailureMessages(List<String> failureMessage) {
        if(this.failureMessages != null) {
            this.failureMessages.addAll(filterEmptyMessages(failureMessage));
        }
        if(this.failureMessages == null) {
            this.failureMessages = filterEmptyMessages(failureMessage);
        }
    }

    private static List<String> filterEmptyMessages(List<String> failureMessage) {
        return failureMessage.stream().filter(message -> !message.isBlank()).toList();
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessage;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static Builder builder() {
        return Builder.builder();
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessage;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessage = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
