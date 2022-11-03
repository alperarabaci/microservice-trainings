package com.training.food.ordering.outbox;

public interface OutboxScheduler {

    void processOutboxMessage();
}
