package com.training.food.ordering.saga;

import com.training.food.ordering.domain.event.DomainEvent;

public interface SagaStep<T, S extends DomainEvent, U extends DomainEvent> {

    S process(T response);

    U rollback(T response);

}
