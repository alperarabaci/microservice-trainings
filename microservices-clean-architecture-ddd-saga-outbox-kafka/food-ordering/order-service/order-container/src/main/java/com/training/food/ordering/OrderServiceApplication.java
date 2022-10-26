package com.training.food.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.training.food.ordering.service.dataaccess", "com.training.food.ordering.dataaccess"})
@EntityScan(basePackages = {"com.training.food.ordering.service.dataaccess", "com.training.food.ordering.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.training.food.ordering")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class);
    }
}