package com.amigoscode.customer.cart;

import com.amigoscode.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findCartById(int id);

    Cart findCartByCustomer(Customer customer);
}
