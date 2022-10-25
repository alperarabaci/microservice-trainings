package com.training.food.ordering.restaurant.service.dataaccess.restaurant.adapter;

import com.training.food.ordering.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.training.food.ordering.restaurant.service.dataaccess.restaurant.repository.OrderApprovalJpaRepository;
import com.training.food.ordering.restaurant.service.domain.entity.OrderApproval;
import com.training.food.ordering.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

}
