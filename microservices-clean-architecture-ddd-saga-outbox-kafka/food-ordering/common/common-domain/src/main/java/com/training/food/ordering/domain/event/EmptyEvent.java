package com.training.food.ordering.domain.event;

public class EmptyEvent implements DomainEvent<Void>{

    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {
    }

    @Override
    public void fire() {

    }

}