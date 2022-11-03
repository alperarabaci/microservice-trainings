package com.training.food.ordering.outbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    public SchedulerConfig() {
        System.out.println("EnableScheduling");
    }
}
