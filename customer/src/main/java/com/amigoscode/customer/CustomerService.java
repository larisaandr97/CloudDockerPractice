package com.amigoscode.customer;

public record CustomerService() {
    public void registerCustomer(final CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        //TODO: check if email valid
        //TODO: check if email not taken
        //TODO: store customer in db
    }
}
