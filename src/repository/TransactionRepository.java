package repository;

import domain.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    // Key i.e., String will be the account number
    private final Map<String, List<Transaction>> txByAccount = new HashMap<>();

    public void add(Transaction transaction) {
        List<Transaction> list = txByAccount.computeIfAbsent(transaction.getAccountNumber(), Key -> new ArrayList<>());
        list.add(transaction);

    }

    public List<Transaction> findByAccount(String account) {

        return new ArrayList<>(txByAccount.getOrDefault(account, Collections.emptyList()));
    }

}
