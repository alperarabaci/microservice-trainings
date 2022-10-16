package com.training.food.order.domain.event.publisher;

import com.training.food.order.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);

}
