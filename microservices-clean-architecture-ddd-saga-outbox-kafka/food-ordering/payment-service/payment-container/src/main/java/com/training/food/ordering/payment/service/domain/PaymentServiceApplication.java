package com.training.food.ordering.payment.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.training.food.ordering.payment.service.dataaccess")
@EntityScan(basePackages = "com.training.food.ordering.payment.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.training.food.ordering")
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class);
    }

}