package com.ascendcorp.exam.model;

public class Bank {
    private String bankCode;
    private String bankNumber;

    public Bank(String bankCode, String bankNumber) {
        this.bankCode = bankCode;
        this.bankNumber = bankNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }
}
