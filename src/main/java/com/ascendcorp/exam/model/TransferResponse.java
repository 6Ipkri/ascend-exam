package com.ascendcorp.exam.model;

import lombok.Data;


@Data
public class TransferResponse {

    private String responseCode;
    private String description;
    private Transaction transaction;
    private Bank bank;
    private Reference reference;
    private String amount;

    public void setTransferCodeDesc(BankResponseCode bankResponseCode){
        this.responseCode = bankResponseCode.getCode();
        this.description = bankResponseCode.getDescription();
    }

     public String getReference1(){
         if(reference == null) return null;
         return reference.getReference1();
    }
    public String getReference2(){
        if(reference == null) return null;
        return reference.getReference2();
    }
    public String getBankTransactionID(){
         if(transaction == null) return null;
         return transaction.getTransactionId();
    }

}
