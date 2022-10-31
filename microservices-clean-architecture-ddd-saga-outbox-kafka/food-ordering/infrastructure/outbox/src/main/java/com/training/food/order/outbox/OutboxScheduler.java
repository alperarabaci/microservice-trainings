package com.training.food.order.outbox;

public interface OutboxScheduler {

    void processOutboxMessage();
}
