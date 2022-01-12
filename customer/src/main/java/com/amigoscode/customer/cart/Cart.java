package com.amigoscode.customer.cart;

import com.amigoscode.customer.entity.Customer;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalAmount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    public Cart() {
    }

    public Cart(int id, double totalAmount, Customer customer) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }

    public Cart(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
