package com.training.food.ordering.application.service.rest;

import com.training.food.ordering.service.domain.dto.create.CreateOrderCommand;
import com.training.food.ordering.service.domain.dto.create.CreateOrderResponse;
import com.training.food.ordering.service.domain.dto.track.TrackOrderQuery;
import com.training.food.ordering.service.domain.dto.track.TrackOrderResponse;
import com.training.food.ordering.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/nvd.api.v1+json")
//@AllArgsConstructor
public class OrderRestController {

    private final OrderApplicationService orderApplicationService;

    public OrderRestController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Creating order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(),
                createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingId());
        return  ResponseEntity.ok(trackOrderResponse);
    }
}

