package com.number26.test.web.rest.errors;


public class CyclicTransactionException extends RuntimeException {

    public CyclicTransactionException() {
        super("Cyclic reference detected. ParentId cant be same as transactionID");
    }
}
