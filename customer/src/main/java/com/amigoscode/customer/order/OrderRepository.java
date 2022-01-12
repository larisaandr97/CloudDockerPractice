package com.amigoscode.customer.order;

import com.amigoscode.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(int id);

    default List<Order> findOrdersByUser(int customerId) {
        return this.findAll().stream().filter(order -> order.getCustomer().getId() == customerId).collect(Collectors.toList());
    }
}
