package com.training.food.ordering.restaurant.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.training.food.ordering.restaurant.service.dataaccess", "com.training.food.ordering.dataaccess"})
@EntityScan(basePackages = {"com.training.food.ordering.restaurant.service.dataaccess", "com.training.food.ordering.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.training.food.ordering")
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class);
    }
}