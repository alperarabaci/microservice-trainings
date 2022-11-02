package com.training.food.ordering.saga;

public interface SagaStep<T> {

    void process(T response);

    void rollback(T response);

}
