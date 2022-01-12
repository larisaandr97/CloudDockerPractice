package com.amigoscode.customer.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int quantity;
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Order order;

//    @ManyToOne(cascade = CascadeType.ALL)
    private int productId;

    public OrderItem() {
    }

    public OrderItem(int quantity, double price, int productId) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
    }
}
