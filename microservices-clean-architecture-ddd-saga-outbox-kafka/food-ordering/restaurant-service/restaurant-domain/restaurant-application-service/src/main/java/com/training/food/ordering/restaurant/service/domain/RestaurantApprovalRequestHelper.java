package com.training.food.ordering.restaurant.service.domain;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import com.training.food.ordering.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.training.food.ordering.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService domainService;
    private final RestaurantDataMapper mapper;
    private final RestaurantRepository repository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher approvedPublisher;
    private final OrderRejectedMessagePublisher rejectedPublisher;

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest request) {
        log.info("Processing restaurant approval for order id: {}", request.getOrderId());
        ArrayList<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(request);
        OrderApprovalEvent event = domainService.validateOrder(restaurant,
                failureMessages,
                approvedPublisher,
                rejectedPublisher);
        orderApprovalRepository.save(restaurant.getOrderApproval());
        return event;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = mapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantResult = repository.findRestaurant(restaurant);
        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with id" + restaurant.getId().getValue() + " not found!");
        }

        Restaurant restaurantEntity = restaurantResult.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }


}
