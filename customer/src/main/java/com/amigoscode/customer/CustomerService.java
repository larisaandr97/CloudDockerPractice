package com.amigoscode.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(final CustomerRegistrationRequest request) {
        final Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        //TODO: check if email valid
        //TODO: check if email not taken
        customerRepository.saveAndFlush(customer);
        //TODO: check if fraudster
        final FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8082/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId());
        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }
        //todo send notification

        //check if the customer has products
        final ProductCheckResponse productCheckResponse = restTemplate.getForObject(
                "http://localhost:8081/api/v1/product-check/{customerId}",
                ProductCheckResponse.class,
                customer.getId());
        if (productCheckResponse.isAnyProduct()) {
            throw new IllegalStateException("no products");
        }

    }
}
