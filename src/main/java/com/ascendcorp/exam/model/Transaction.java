package com.ascendcorp.exam.model;

import org.mockito.ArgumentMatcher;

import java.util.Date;

public class Transaction implements ArgumentMatcher<Transaction> {
    private String transactionId;
    private Date tranDateTime;
    private String channel;
    private String locationCode;
    public Transaction(String transactionId,
                       Date tranDateTime,
                       String channel,
                       String locationCode){
        this.transactionId = transactionId;
        this.tranDateTime = tranDateTime;
        this.channel = channel;
        this.locationCode = locationCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTranDateTime() {
        return tranDateTime;
    }

    public void setTranDateTime(Date tranDateTime) {
        this.tranDateTime = tranDateTime;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public boolean matches(Transaction t) {
        return false;
    }
}
