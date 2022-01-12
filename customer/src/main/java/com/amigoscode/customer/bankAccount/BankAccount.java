package com.amigoscode.customer.bankAccount;

import com.amigoscode.customer.entity.Customer;
import com.amigoscode.customer.order.Order;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "accountNumber")
    private String accountNumber;
    private double balance;
    @Column(name = "cardNumber")
    private String cardNumber;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    private List<Order> orders;

    public BankAccount() {
    }

    public BankAccount(String accountNumber, double balance, String cardNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }

    public BankAccount(int id, String accountNumber, double balance, String cardNumber, Customer customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }
}
