package com.training.food.ordering.saga;

import com.training.food.ordering.domain.event.DomainEvent;

public interface SagaStep<T> {

    void process(T response);

    void rollback(T response);

}
