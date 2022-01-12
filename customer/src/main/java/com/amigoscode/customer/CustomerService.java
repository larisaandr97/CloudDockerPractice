package com.amigoscode.customer;

import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.exception.CustomerAlreadyExistException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public Customer findByUsername(final String username) {
        return customerRepository.findByUsername(username);
    }

    public void registerNewCustomer(final Customer customer) {
        if (emailExists(customer.getEmail())) {
            throw new CustomerAlreadyExistException("There is an account with that email address: " + customer.getEmail());
        }
        customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Customer customer = customerRepository.findByUsername(username);
        if (customer == null)
            throw new UsernameNotFoundException("User not found.");
        return customer;
    }

    private boolean emailExists(String email) {
        return customerRepository.findByEmail(email) != null;
    }

    public void registerCustomer(final CustomerRegistrationRequest request) {
        final Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        //TODO: check if email valid
        //TODO: check if email not taken
        customerRepository.saveAndFlush(customer);
        //todo send notification

        //check if the customer has products
//        final ProductCheckResponse productCheckResponse = restTemplate.getForObject(
//                "http://localhost:8082/api/v1/product-check/{customerId}",
//                ProductCheckResponse.class,
//                customer.getId());
//        if (productCheckResponse.isAnyProduct()) {
//            throw new IllegalStateException("no products");
//        }

    }
}
