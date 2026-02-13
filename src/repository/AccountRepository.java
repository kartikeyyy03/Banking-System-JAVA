package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import domain.Account;
import domain.Customer;

public class AccountRepository {
    private final Map<String, Account> accountsByNumber = new HashMap<>();

    public void save(Account account) {
        accountsByNumber.put(account.getAccountNumber(), account);

    }

    public List<Account> findAll() {
        return new ArrayList<>(accountsByNumber.values());
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerId(String CustomerId) {

        List<Account> result = new ArrayList<>();

        for (Account a : accountsByNumber.values()) {
            if (a.getCustomerId().equals(CustomerId)) {

                result.add(a);

            }
        }
        return result;

    }

}
