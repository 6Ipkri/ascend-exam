package com.ascendcorp.exam.service;

import com.ascendcorp.exam.model.TransferResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class BankService {



    public TransferResponse requestTransfer(
            String transactionId, Date tranDateTime, String channel,
            String bankCode, String bankNumber, double amount,
            String reference1, String reference2) {

        //TODO call web service
        return new TransferResponse();
    }
}

