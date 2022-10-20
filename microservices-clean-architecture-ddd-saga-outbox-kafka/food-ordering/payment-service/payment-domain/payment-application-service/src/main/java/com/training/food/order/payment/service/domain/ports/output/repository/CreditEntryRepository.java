package com.training.food.order.payment.service.domain.ports.output.repository;

import com.training.food.order.domain.valueobject.CustomerId;
import com.training.food.ordering.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
