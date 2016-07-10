package com.number26.test.web.rest;

import com.number26.test.service.TransactionService;
import com.number26.test.web.rest.dto.SumAmountDto;
import com.number26.test.web.rest.dto.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
public class TransactionResource {

    @Inject
    private TransactionService transactionService;

    @RequestMapping(value = "/transactionservice/transaction/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> createTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionDto) {
        transactionService.addTransaction(id, transactionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/transactionservice/transaction/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.get(id));
    }

    @RequestMapping(value = "/transactionservice/types/{type}", method = RequestMethod.GET)
    public ResponseEntity<?> getTransactionsByType(@PathVariable String type) {
        return ResponseEntity.ok(transactionService.findAllByType(type));
    }

    @RequestMapping(value = "/transactionservice/sum/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSum(@PathVariable Long id) {
        return ResponseEntity.ok(new SumAmountDto(transactionService.getSumForLinkedTransactions(id)));
    }

}
