package com.amigoscode.customer.order;

import com.amigoscode.customer.bankAccount.BankAccount;
import com.amigoscode.customer.bankAccount.BankAccountService;
import com.amigoscode.customer.cart.Cart;
import com.amigoscode.customer.cart.CartService;
import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.exception.BankAccountNotBelongingToCustomer;
import com.amigoscode.customer.exception.CartIsEmptyException;
import com.amigoscode.customer.exception.InsufficientFundsException;
import com.amigoscode.customer.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BankAccountService bankAccountService;
    private final OrderItemRepository orderItemRepository;
    //    private final ProductService productService;
    private final CartService cartService;
    private final RestTemplate restTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, BankAccountService bankAccountService, OrderItemRepository orderItemRepository, CartService cartService, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.bankAccountService = bankAccountService;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.restTemplate = restTemplate;
    }

    @Override
    public Order findOrderById(int id) {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findOrderById(id));
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new ResourceNotFoundException("Order with Id " + id + " not found.");
        }
    }

    @Override
    public List<Order> getOrdersByUser(Customer customer) {
        return orderRepository.findOrdersByUser(customer.getId());
    }

    @Transactional
    @Override
    public Optional<Order> createOrder(Customer customer, List<OrderItemRequest> orderItemRequests, int accountId) {

        BankAccount bankAccount = validateBankAccount(customer.getId(), accountId);

        Cart cart = cartService.findCartByUser(customer);
        if (cart == null)
            throw new ResourceNotFoundException("Cart for customer with Id " + customer.getId() + " not found.");
        if (cart.getTotalAmount() == 0)
            throw new CartIsEmptyException(customer.getId());

        // Creating order object
        Order order = new Order();
        order.setCustomer(customer);
        order.setAccount(bankAccount);

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest item : orderItemRequests) {
            final int stock = restTemplate.getForObject(
                    "http://localhost:8082/products/getStock/{productId}",
                    int.class,
                    item.getProductId());
//            Product product = productService.findProductById(item.getProduct().getId());
//            int stock = product.getStock();
            // if there is available stock for the product, we add to list of order items
            if (stock >= item.getQuantity()) {
                restTemplate.getForObject(
                        "http://localhost:8082/products/updateStock?productId={productId}&stock={stock}",
                        double.class,
                        item.getProductId(), stock - item.getQuantity());
//                productService.updateStock(item.getProductId(), stock - item.getQuantity());
                total += item.getPrice() * item.getQuantity();
                orderItems.add(new OrderItem(item.getQuantity(), item.getPrice(), item.getProductId()));
            }
        }

        if (total == 0)
            return Optional.empty();
        // check if there is enough money in the account to pay the order
        boolean result = checkBalanceForOrder(bankAccount, total);
        order.setTotalAmount(total);
        order.setDatePlaced(LocalDate.now());

        // Saving entities in database
        orderRepository.save(order);
        orderItems.forEach(item -> {
            item.setOrder(order);
            orderItemRepository.save(item);
        });

        // withdraw money from bank account
        bankAccountService.withdrawMoneyFromAccount(bankAccount.getId(), bankAccount.getBalance() - total);

        cartService.resetCart(cart);
        return Optional.of(order);
    }

    @Override
    public boolean checkBalanceForOrder(BankAccount bankAccount, double total) {
        if (bankAccount.getBalance() < total)
            throw new InsufficientFundsException(bankAccount.getId());
        else return true;
    }

    @Override
    public BankAccount validateBankAccount(int userId, int accountId) {
        BankAccount bankAccount = bankAccountService.findBankAccountById(accountId);
        if (userId != bankAccount.getCustomer().getId())
            throw new BankAccountNotBelongingToCustomer(accountId, userId);
        return bankAccount;
    }
}
