package com.number26.test.service;


import com.number26.test.model.Transaction;
import com.number26.test.repository.TransactionRepository;
import com.number26.test.web.rest.dto.TransactionDto;
import com.number26.test.web.rest.errors.CyclicTransactionException;
import com.number26.test.web.rest.errors.NoSuchTransactionException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionService {

    @Inject
    private TransactionRepository transactionRepository;


    public Transaction addTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setType(transactionDto.getType());

        Long parentId = transactionDto.getParentId();
        if (parentId != null) {
            if (id.equals(parentId)) {
                throw new CyclicTransactionException();
            }

            Transaction parent = get(parentId);
            parent.setChildTransaction(transaction);
        }

        transactionRepository.save(transaction);

        return transaction;
    }

    public Transaction get(Long id) {
        return Optional.ofNullable(transactionRepository.findOne(id))
                .orElseThrow(() -> new NoSuchTransactionException(id));
    }

    public Set<Long> findAllByType(String type) {
        return transactionRepository.findAllByType(type);
    }

    public Double getSumForLinkedTransactions(Long transactionId) {
        Double sum = 0.0;

        Transaction transaction = get(transactionId);

        while (transaction != null) {
            sum += transaction.getAmount();
            transaction = transaction.getChildTransaction();
        }

        return sum;
    }


}
