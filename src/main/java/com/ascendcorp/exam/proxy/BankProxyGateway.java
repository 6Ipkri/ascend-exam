package com.ascendcorp.exam.proxy;

import com.ascendcorp.exam.model.Bank;
import com.ascendcorp.exam.model.Reference;
import com.ascendcorp.exam.model.Transaction;
import com.ascendcorp.exam.model.TransferResponse;


public class BankProxyGateway {

    public TransferResponse requestTransfer(Transaction transaction, Bank bank, double amount, Reference reference) {

        return new TransferResponse();
    }
}

