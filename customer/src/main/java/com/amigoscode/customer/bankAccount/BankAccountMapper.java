package com.amigoscode.customer.bankAccount;

import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccount bankAccountRequestToBankAccount(BankAccountRequest bankAccountRequest) {
        return new BankAccount(bankAccountRequest.getAccountNumber(),
                bankAccountRequest.getBalance(),
                bankAccountRequest.getCardNumber(), bankAccountRequest.getCustomer());
    }
}
