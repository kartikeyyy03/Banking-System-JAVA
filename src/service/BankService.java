package service;

import domain.Account;
import domain.Transaction;

import java.util.List;

public interface BankService {
    String openAccount(String name, String email, String accountType, double initial);

    List<Account> listAccounts();

    void deposit(String accountNumber, Double amount, String note);

    void withdraw(String accountNumber, double amount, String note);

    void transfer(String from, String to, double amount, String note);

    List<Transaction> getStatement(String account);

    List<Account> searchAccountsByCustomerName(String q);
}
