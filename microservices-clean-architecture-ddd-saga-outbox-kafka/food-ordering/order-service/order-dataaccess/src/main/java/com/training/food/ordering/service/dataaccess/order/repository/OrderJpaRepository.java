package com.training.food.ordering.service.dataaccess.order.repository;

import com.training.food.ordering.domain.valueobject.OrderId;
import com.training.food.ordering.service.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID > {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);


    Optional<OrderEntity>  findById(OrderId orderId);
}
