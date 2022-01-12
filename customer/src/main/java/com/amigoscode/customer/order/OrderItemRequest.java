package com.amigoscode.customer.order;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int productId;
    private int quantity;
    private double price;

    public OrderItemRequest(int productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

}
