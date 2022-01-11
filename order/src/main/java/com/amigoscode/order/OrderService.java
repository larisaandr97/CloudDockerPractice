package com.amigoscode.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(final OrderRequest request) {
        final Order order = Order.builder()
                .items(request.items())
                .customerId(request.customerId())
                .build();
        //TODO: check if email valid
        //TODO: check if email not taken
        orderRepository.saveAndFlush(order);
        //TODO: check if fraudster
        final CustomerCheckResponse customerCheckResponse = restTemplate.getForObject(
                "http://localhost:8080/api/v1/customer/{customerId}",
                CustomerCheckResponse.class,
                order.getCustomerId());
        if (customerCheckResponse.id() != null) {
            System.out.println(customerCheckResponse.id());

        }
    }

}
