package com.training.food.order.service.domain;

import com.training.food.order.domain.event.publisher.DomainEventPublisher;
import com.training.food.ordering.order.service.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements ApplicationEventPublisherAware, DomainEventPublisher<OrderCreatedEvent> {

    private ApplicationEventPublisher publisher;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        this.publisher.publishEvent(domainEvent);
        log.info("OrderCreatedEvent is published for order id, {}", domainEvent.getOrder().getId().getValue());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
