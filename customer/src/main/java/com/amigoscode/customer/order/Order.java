package com.amigoscode.customer.order;

import com.amigoscode.customer.bankAccount.BankAccount;
import com.amigoscode.customer.entity.Customer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "totalAmount")
    private double totalAmount;
    private LocalDate datePlaced;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne()
    private BankAccount account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(double totalAmount, LocalDate datePlaced, Customer customer) {
        this.totalAmount = totalAmount;
        this.datePlaced = datePlaced;
        this.customer = customer;
    }
}
