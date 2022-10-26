package com.training.food.ordering.service.domain;

import com.training.food.ordering.order.service.domain.OrderDomainService;
import com.training.food.ordering.order.service.domain.OrderDomainServiceImpl;
import com.training.food.ordering.service.domain.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import com.training.food.ordering.service.domain.ports.output.repository.CustomerRepository;
import com.training.food.ordering.service.domain.ports.output.repository.OrderRepository;
import com.training.food.ordering.service.domain.ports.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.training.food.order")
public class OrderTestConfiguration {

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderCreatePaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreatePaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }


}
