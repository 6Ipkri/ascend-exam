package com.ascendcorp.exam.model;

import lombok.Data;


@Data
public class Bank {

    private String bankCode;
    private String bankNumber;

    public Bank(String bankCode, String bankNumber) {
        this.bankCode = bankCode;
        this.bankNumber = bankNumber;
    }
}
