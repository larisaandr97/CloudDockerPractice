package com.amigoscode.customer.bankAccount;


import com.amigoscode.customer.entity.Customer;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.amigoscode.customer.entity.Pattern.VISA_CREDIT_CARD;

@Data
public class BankAccountRequest {

    @NotBlank(message = "Account number cannot be null.")
    private String accountNumber;

    @NotNull
    @Min(0)
    private double balance;

    @Pattern(regexp = VISA_CREDIT_CARD, message = "Card number not valid! Only Visa accepted.")
    private String cardNumber;

    // @JsonIgnore
    private Customer customer;

    public BankAccountRequest() {
    }

    public BankAccountRequest(String accountNumber, double balance, String cardNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.customer = customer;
    }
}
