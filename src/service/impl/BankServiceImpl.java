package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

// import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
// import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

        private final AccountRepository accountRepository = new AccountRepository();
        private final TransactionRepository transactionRepository = new TransactionRepository();
        private final CustomerRepository customerRepository = new CustomerRepository();

        private final Validation<String> validateName = name -> {
                if (name == null || name.isBlank())
                        throw new ValidationException("Name is required!");
        };

        private final Validation<String> validateEmail = email -> {

                if (email == null || !email.contains("@"))
                        throw new ValidationException("Invalid Email!");
        };

        private final Validation<String> validateType = type -> {

                if (type == null || (!type.equalsIgnoreCase("SAVINGS") && !type.equalsIgnoreCase("CURRENT")))
                        throw new ValidationException("Invalid Account Type! Must be SAVINGS or CURRENT");
        };
        private final Validation<Double> validateAmountPositive = amount -> {

                if (amount == null || amount < 0)
                        throw new ValidationException("PLease enter a valid amount!");
        };

        @Override
        public String openAccount(String name, String email, String accountType, double initial) {

                validateName.validate(name);
                validateEmail.validate(email);
                validateType.validate(accountType);
                validateAmountPositive.validate(initial);

                String customerId = UUID.randomUUID().toString();

                // CREATE CUSTOMER:
                Customer c = new Customer(customerId, name, email);
                customerRepository.save(c);

                // change later --> total number of accounts + 1. eg 10+1=AC000011 --> AC<06>
                // String accountNumber = UUID.randomUUID().toString();

                String accountNumber = getAccountNumber();

                Account account = new Account(accountNumber, customerId, initial, accountType);

                // SAVE
                accountRepository.save(account);

                Transaction transaction = new Transaction(account.getAccountNumber(), initial,
                                UUID.randomUUID().toString(),
                                "Initial Deposit", LocalDateTime.now(), Type.DEPOSIT);
                transactionRepository.add(transaction);

                return accountNumber;

        }

        private String getAccountNumber() {
                int size = accountRepository.findAll().size() + 1;
                return String.format("AC%06d", size);

        }

        @Override
        public List<Account> listAccounts() {
                return accountRepository.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber))
                                .collect(Collectors.toList());
        }

        @Override
        public void deposit(String accountNumber, Double amount, String note) {

                validateAmountPositive.validate(amount);

                Account account = accountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

                account.setBalance(account.getBalance() + amount);
                Transaction transaction = new Transaction(account.getAccountNumber(), amount,
                                UUID.randomUUID().toString(),
                                note, LocalDateTime.now(), Type.DEPOSIT);
                transactionRepository.add(transaction);
        }

        @Override
        public void withdraw(String accountNumber, double amount, String note) {

                validateAmountPositive.validate(amount);

                Account account = accountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

                if (amount > account.getBalance()) {
                        throw new InsufficientFundsException(
                                        "Insufficient Balance. Maximum amount withdrawable: " + account.getBalance());

                }

                account.setBalance(account.getBalance() - amount);
                Transaction transaction = new Transaction(account.getAccountNumber(), amount,
                                UUID.randomUUID().toString(),
                                note, LocalDateTime.now(), Type.WITHDRAW);
                transactionRepository.add(transaction);

        }

        @Override
        public void transfer(String fromAcc, String toAcc, double amount, String note) {

                validateAmountPositive.validate(amount);

                if (fromAcc.equals(toAcc)) {
                        throw new ValidationException("Can not transfer to the same account!");
                }

                Account from = accountRepository.findByAccountNumber(fromAcc)
                                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + fromAcc));

                Account to = accountRepository.findByAccountNumber(toAcc)
                                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + toAcc));

                if (amount > from.getBalance()) {
                        throw new InsufficientFundsException(
                                        "Insufficient Balance. Maximum amount withdrawable from Account " + fromAcc
                                                        + ": " + from.getBalance());

                }

                from.setBalance(from.getBalance() - amount);
                to.setBalance(to.getBalance() + amount);

                transactionRepository.add(new Transaction(from.getAccountNumber(), amount, UUID.randomUUID().toString(),
                                note, LocalDateTime.now(), Type.TRANSFER_OUT));

                transactionRepository.add(new Transaction(to.getAccountNumber(), amount, UUID.randomUUID().toString(),
                                note, LocalDateTime.now(), Type.TRANSFER_IN));

        }

        @Override
        public List<Transaction> getStatement(String account) {

                return transactionRepository.findByAccount(account).stream()
                                .sorted(Comparator.comparing(Transaction::getTimeStamp)).collect(Collectors.toList());

        }

        @Override
        public List<Account> searchAccountsByCustomerName(String q) {

                String query = q == null ? "" : q.toLowerCase();

                // List<Account> result = new ArrayList<>();

                // for (Customer c : customerRepository.findAll()) {
                // if (c.getName().toLowerCase().contains(query)) {

                // result.addAll(accountRepository.findByCustomerId(c.getId()));

                // }
                // }
                // result.sort(Comparator.comparing(Account::getAccountNumber));
                // return result;

                return customerRepository.findAll().stream().filter(c -> c.getName().toLowerCase().contains(query))
                                .flatMap(c -> accountRepository.findByCustomerId(c.getId()).stream())
                                .sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());

        }

}
