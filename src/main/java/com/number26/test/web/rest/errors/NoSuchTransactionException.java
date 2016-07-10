package com.number26.test.web.rest.errors;

public class NoSuchTransactionException extends RuntimeException {

    public NoSuchTransactionException(Long id) {
        super("No such transaction : " + id);
    }
}
