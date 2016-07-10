package com.number26.test.web.rest.dto;

public class SumAmountDto {

    public SumAmountDto() {
    }

    public SumAmountDto(Double sum) {
        this.sum = sum;
    }

    private Double sum;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
