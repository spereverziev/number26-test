package com.number26.test.repository;

import com.number26.test.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TransactionRepository {

    private Map<Long, Transaction> transactions = new HashMap<>();

    public void save(Transaction transaction) {
        if (transaction == null) {
            throw new NullPointerException("Transaction shouldn't be null");
        }

        if (transaction.getId() == null) {
            throw new IllegalArgumentException("Transaction Id cannot be null");
        }

        transactions.put(transaction.getId(), transaction);
    }

    public Transaction findOne(Long id) {
        return transactions.get(id);
    }

    public Collection<Transaction> findAll() {
        return transactions.values();
    }

    public Set<Long> findAllByType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Type shouldn't be null");
        }

        return transactions.values()
                .stream()
                .filter(transaction -> transaction.getType().equals(type.toLowerCase()))
                .map(Transaction::getId)
                .collect(Collectors.toSet());
    }

    public void deleteAll() {
        transactions.clear();
    }

}
