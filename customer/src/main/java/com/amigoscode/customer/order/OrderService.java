package com.amigoscode.customer.order;

import com.amigoscode.customer.bankAccount.BankAccount;
import com.amigoscode.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order findOrderById(int id);

    List<Order> getOrdersByUser(Customer customer);

    Optional<Order> createOrder(Customer customer, List<OrderItemRequest> orderItemRequests, int accountId);

    boolean checkBalanceForOrder(BankAccount bankAccount, double total);

    BankAccount validateBankAccount(int userId, int accountId);
}
