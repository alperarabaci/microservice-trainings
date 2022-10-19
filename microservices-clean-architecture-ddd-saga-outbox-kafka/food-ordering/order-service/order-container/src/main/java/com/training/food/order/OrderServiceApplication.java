package com.training.food.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

@EnableJpaRepositories(basePackages = "com.training.food.order.service.dataaccess")
@EntityScan(basePackages = "com.training.food.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.training.food.order")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class);
    }
}