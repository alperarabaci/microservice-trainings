package com.training.food.order.service.domain;

import com.training.food.order.service.domain.dto.create.CreateOrderCommand;
import com.training.food.order.service.domain.dto.create.CreateOrderResponse;
import com.training.food.order.service.domain.mapper.OrderDataMapper;
import com.training.food.order.service.domain.ports.output.repository.CustomerRepository;
import com.training.food.order.service.domain.ports.output.repository.OrderRepository;
import com.training.food.order.service.domain.ports.output.repository.RestaurantRepository;
import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.entity.Customer;
import com.training.food.ordering.order.service.domain.entity.Order;
import com.training.food.ordering.order.service.domain.entity.Restaurant;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import com.training.food.ordering.order.service.domain.exception.OrderDomainException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommonHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    private final ApplicationDomainEventPublisher publisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = saveOrder(order);
        log.info("Order is created with id: {}", orderResult.getId().getValue());
        publisher.publish(event);
        return orderDataMapper.orderToCreateOrderResponse(orderResult, null);
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if(orderResult == null) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if(optRestaurant.isEmpty()){
            throw new OrderDomainException("Could not find restaurant with id " +
                    createOrderCommand.getRestaurantId());
        }
        return optRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if(customer.isEmpty()) {
            log.warn("Could not find customer with custoemr id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id" + customerId);
        }
    }
}
