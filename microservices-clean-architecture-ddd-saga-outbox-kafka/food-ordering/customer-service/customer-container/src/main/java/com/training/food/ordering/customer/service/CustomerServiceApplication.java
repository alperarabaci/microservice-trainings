package com.training.food.ordering.customer.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.training.food.order.customer.service.dataaccess")
@EntityScan(basePackages = "com.training.food.order.customer.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.training.food.order")
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class);
    }
}