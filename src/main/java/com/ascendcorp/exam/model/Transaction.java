package com.ascendcorp.exam.model;

import lombok.Data;

import java.util.Date;


@Data
public class Transaction {

    private String transactionId;
    private Date tranDateTime;
    private String channel;
    private String locationCode;

    public Transaction(String transactionId, Date tranDateTime, String channel, String locationCode){
        this.transactionId = transactionId;
        this.tranDateTime = tranDateTime;
        this.channel = channel;
        this.locationCode = locationCode;
    }
}
