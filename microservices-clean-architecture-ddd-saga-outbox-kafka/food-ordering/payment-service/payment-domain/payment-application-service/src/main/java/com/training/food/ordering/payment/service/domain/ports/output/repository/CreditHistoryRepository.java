package com.training.food.ordering.payment.service.domain.ports.output.repository;

import com.training.food.ordering.domain.valueobject.CustomerId;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
