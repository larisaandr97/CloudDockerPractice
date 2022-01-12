package com.amigoscode.customer.bankAccount;


import com.amigoscode.customer.entity.Customer;

import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccount bankAccount);

    List<BankAccount> getBankAccountsForUser(Customer customer);

    BankAccount findBankAccountById(int id);

    void withdrawMoneyFromAccount(int accountId, double balance);

}
