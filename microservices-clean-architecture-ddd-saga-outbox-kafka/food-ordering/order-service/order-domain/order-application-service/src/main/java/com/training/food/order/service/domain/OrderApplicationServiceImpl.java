package com.training.food.order.service.domain;

import com.training.food.order.service.domain.dto.create.CreateOrderCommand;
import com.training.food.order.service.domain.dto.create.CreateOrderResponse;
import com.training.food.order.service.domain.dto.track.TrackOrderQuery;
import com.training.food.order.service.domain.dto.track.TrackOrderResponse;
import com.training.food.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
@AllArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderTrackCommandHandler orderTrackCommandHandler;
    private final OrderCreateCommonHandler orderCreateCommonHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommonHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
