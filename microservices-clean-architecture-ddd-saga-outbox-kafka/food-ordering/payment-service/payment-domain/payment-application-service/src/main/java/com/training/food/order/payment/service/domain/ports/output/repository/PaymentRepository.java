package com.training.food.order.payment.service.domain.ports.output.repository;

import com.training.food.ordering.payment.service.domain.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID uuid);
}
