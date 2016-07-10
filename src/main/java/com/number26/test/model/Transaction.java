package com.number26.test.model;

public class Transaction {

    private Long id;

    private Double amount;

    private String type;

    private Transaction childTransaction;

    public Transaction getChildTransaction() {
        return childTransaction;
    }

    public void setChildTransaction(Transaction childTransaction) {
        this.childTransaction = childTransaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
