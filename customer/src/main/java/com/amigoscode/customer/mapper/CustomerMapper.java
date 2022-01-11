package com.amigoscode.customer.mapper;

import com.amigoscode.customer.dto.CustomerRequest;
import com.amigoscode.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer customerRequestToCustomer(CustomerRequest userRequest) {
        return new Customer(userRequest.getUsername(), passwordEncoder.encode(userRequest.getPassword()), userRequest.getEmail(), userRequest.getFirstName(), userRequest.getLastName());
    }
}
