package com.training.food.ordering.restaurant.service.domain;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.outbox.OutboxStatus;
import com.training.food.ordering.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.training.food.ordering.restaurant.service.domain.entity.Restaurant;
import com.training.food.ordering.restaurant.service.domain.event.OrderApprovalEvent;
import com.training.food.ordering.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.training.food.ordering.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.training.food.ordering.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.training.food.ordering.restaurant.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.training.food.ordering.restaurant.service.domain.ports.output.message.publisher.RestaurantApprovalResponseMessagePublisher;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    private final OrderOutboxHelper orderOutboxHelper;

    private final RestaurantApprovalResponseMessagePublisher publisher;
    @Transactional
    public void persistOrderApproval(RestaurantApprovalRequest request)  {
        if (publishIfOutboxMessageProcessed(request)) {
            log.info("An outbox message with saga id: {} already saved to database!",
                    request.getSagaId());
            return;
        }

        log.info("Processing restaurant approval for order id: {}", request.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(request);
        OrderApprovalEvent orderApprovalEvent =
                domainService.validateOrder(
                        restaurant,
                        failureMessages);
        orderApprovalRepository.save(restaurant.getOrderApproval());

        orderOutboxHelper
                .saveOrderOutboxMessage(mapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
                        orderApprovalEvent.getOrderApproval().getApprovalStatus(),
                        OutboxStatus.STARTED,
                        UUID.fromString(request.getSagaId()));

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

    private boolean publishIfOutboxMessageProcessed(RestaurantApprovalRequest restaurantApprovalRequest) {
        Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(UUID
                        .fromString(restaurantApprovalRequest.getSagaId()), OutboxStatus.COMPLETED);
        if (orderOutboxMessage.isPresent()) {
            publisher.publish(orderOutboxMessage.get(),
                    orderOutboxHelper::updateOutboxStatus);
            return true;
        }
        return false;
    }


}
