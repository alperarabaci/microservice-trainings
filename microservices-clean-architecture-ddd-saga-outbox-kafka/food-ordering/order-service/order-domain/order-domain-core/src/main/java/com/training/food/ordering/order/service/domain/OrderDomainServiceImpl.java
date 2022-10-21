package com.training.food.ordering.order.service.domain;

import com.training.food.ordering.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.entity.Product;
import com.training.food.ordering.order.service.domain.entity.Restaurant;
import com.training.food.ordering.order.service.domain.event.OrderCancelledEvent;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import com.training.food.ordering.order.service.domain.event.OrderPaidEvent;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

    public static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                      Restaurant restaurant,
                                                      DomainEventPublisher<OrderCreatedEvent> publisher) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initialized", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), publisher);
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> {
            restaurant.getProducts().forEach(restaurantProduct -> {
                Product currentProduct = orderItem.getProduct();
                if(currentProduct.equals(restaurantProduct)) {
                    currentProduct.updateWithConfirmedNamedAndPrice(restaurantProduct.getName(),
                            restaurantProduct.getPrice());
                }
            });
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() + " is currently not active!");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return null;
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved {}", order.getItems(), order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order,
                                                  List<String> failureMessages,
                                                  DomainEventPublisher<OrderCancelledEvent> publisher) {
        order.initCancel(failureMessages);
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), publisher);
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} is canceled.", order.getId().getValue());
    }
}
