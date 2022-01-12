package com.amigoscode.customer.cart;

import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.order.OrderItemRequest;

import java.util.List;

public interface CartService {
    Cart findCartByUser(Customer customer);

    Cart createCart(Customer customer, double value, OrderItemRequest item);

    void resetCart(Cart cart);

    OrderItemRequest getItemByProductId(int productId, int userId);

    int updateItemQuantity(int userId, OrderItemRequest item, int quantity);

    List<OrderItemRequest> deleteItemFromCart(Cart cart, int userId, int productId);

    List<OrderItemRequest> getCartContents(int userId);

//    Product validateProduct(int productId, int quantity);

    Cart addProductToCart(Customer customer, OrderItemRequest item);

    void updateCartAmount(int cartId, double value);
}
