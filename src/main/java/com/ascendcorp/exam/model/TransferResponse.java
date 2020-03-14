package com.ascendcorp.exam.model;

public class TransferResponse {

    private String responseCode;
    private String description;
    private Transaction transaction;
    private Bank bank;
    private Reference reference;
    private String amount;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceCode1() {
        if(reference == null) return null;
        return reference.getReference1();

    }

    public String getReferenceCode2() {
        if(reference == null) return null;
        return reference.getReference2();
    }

    public String getBalance() {
        return amount;
    }

    public String getBankTransactionID() {
        if(transaction == null) return null;
        return transaction.getTransactionId();
    }

}
