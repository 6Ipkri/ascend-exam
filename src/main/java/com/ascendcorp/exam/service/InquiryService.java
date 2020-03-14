package com.ascendcorp.exam.service;

import com.ascendcorp.exam.model.*;
import com.ascendcorp.exam.proxy.BankProxyGateway;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.WebServiceException;


public class InquiryService {

    @Autowired
    private BankProxyGateway bankProxyGateway;

    public InquiryServiceResultDTO inquiry(Transaction transaction, Bank bank, double amount, Reference reference) {
        InquiryServiceResultDTO respDTO = null;
        try {
            checkInputNotNull(transaction, bank, amount, reference);
            TransferResponse response = bankProxyGateway.requestTransfer(transaction, bank, amount, reference);

            if (response != null) {
                respDTO = new InquiryServiceResultDTO();

                respDTO.setRef_no1(response.getReference1());
                respDTO.setRef_no2(response.getReference2());
                respDTO.setAmount(response.getAmount());
                respDTO.setTranID(response.getBankTransactionID());

                if (response.getResponseCode().equalsIgnoreCase("approved")) {
                    responseApproved(respDTO, response);

                } else if (response.getResponseCode().equalsIgnoreCase("invalid_data")) {
                    responseInvalidData(respDTO, response);

                } else if (response.getResponseCode().equalsIgnoreCase("transaction_error")) {
                    responseTransactionError(respDTO, response);

                } else if (response.getResponseCode().equalsIgnoreCase("unknown")) {
                    responseUnknown(respDTO, response);

                } else
                    throw new Exception("Unsupport Error Reason Code");
            } else
                throw new Exception("Unable to inquiry from service.");
        } catch (NullPointerException ne) {
            if (respDTO == null) {
                respDTO = new InquiryServiceResultDTO();

                respDTO.setResponse(BankResponseCode.RE_501_GENERAL_INVALID_DATA.getCode(), BankResponseCode.RE_501_GENERAL_INVALID_DATA.getDescription());
            }
        } catch (WebServiceException r) {
            String faultString = r.getMessage();
            if (respDTO == null) {
                respDTO = new InquiryServiceResultDTO();
                if (faultString != null && (faultString.contains("java.net.SocketTimeoutException")
                        || faultString.contains("Connection timed out"))) {
                    respDTO.setResponse(BankResponseCode.RE_503_ERROR_TIME_OUT.getCode(), BankResponseCode.RE_503_ERROR_TIME_OUT.getDescription());
                } else {
                    respDTO.setResponse(BankResponseCode.RE_504_INTERNAL_ERROR.getCode(), BankResponseCode.RE_504_INTERNAL_ERROR.getDescription());
                }
            }
        } catch (Exception e) {
            if (respDTO == null || respDTO.getReasonCode() == null) {
                respDTO = new InquiryServiceResultDTO();
                respDTO.setResponse(BankResponseCode.RE_504_INTERNAL_ERROR.getCode(), BankResponseCode.RE_504_INTERNAL_ERROR.getDescription());
            }
        }
        return respDTO;
    }

    private void checkInputNotNull(Transaction transaction, Bank bank, double amount, Reference reference) throws NullPointerException {

        if (transaction.getTransactionId() == null) {
            throw new NullPointerException("Transaction id is required!");
        }
        if (transaction.getTranDateTime() == null) {
            throw new NullPointerException("Transaction DateTime is required!");
        }
        if (transaction.getChannel() == null) {
            throw new NullPointerException("Channel is required!");
        }
        if (bank.getBankCode() == null || bank.getBankCode().equalsIgnoreCase("")) {
            throw new NullPointerException("Bank Code is required!");
        }
        if (bank.getBankNumber() == null || bank.getBankNumber().equalsIgnoreCase("")) {
            throw new NullPointerException("Bank Number is required!");
        }
        if (amount <= 0) {
            throw new NullPointerException("Amount must more than zero!");
        }
    }

    private void responseApproved(InquiryServiceResultDTO respDTO, TransferResponse response) {
        respDTO.setResponse(BankResponseCode.RE_200_APPROVED.getCode(), BankResponseCode.RE_200_APPROVED.getDescription());
        respDTO.setAccountName(response.getDescription());
    }

    private void responseInvalidData(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        BankResponseCode generalTransactionError = BankResponseCode.RE_501_GENERAL_INVALID_DATA;
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 3) {
                respDTO.setResponse(respDesc[1], respDesc[2]);
            } else {
                respDTO.setResponse(generalTransactionError.getCode(), generalTransactionError.getDescription());
            }
        } else {
            respDTO.setResponse(generalTransactionError.getCode(), generalTransactionError.getDescription());
        }
    }

    private void responseTransactionError(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        String subIdx1, subIdx2, subIdx3;
        BankResponseCode generalTransactionError = BankResponseCode.RE_500_GENERAL_TRANSACTION;
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            subIdx1 = respDesc[0];

            if (respDesc.length == 1) {
                respDTO.setResponse(generalTransactionError.getCode(), generalTransactionError.getDescription());
            } else if (respDesc.length == 2) {
                subIdx2 = respDesc[1];
                respDTO.setResponse(subIdx1, subIdx2);
            } else {
                subIdx2 = respDesc[1];
                subIdx3 = respDesc[2];

                respDTO.setResponse(subIdx2, subIdx3);
            }
        } else {
            respDTO.setResponse(generalTransactionError.getCode(), generalTransactionError.getDescription());
        }
    }

    private void responseUnknown(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        BankResponseCode invalidData = BankResponseCode.RE_501_GENERAL_INVALID_DATA;
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 2) {
                respDTO.setResponse(respDesc[0], respDesc[1]);
                if (respDTO.getReasonDesc() == null || respDTO.getReasonDesc().trim().length() == 0) {
                    respDTO.setReasonDesc("General Invalid Data");
                }
            } else {
                respDTO.setResponse(invalidData.getCode(), invalidData.getDescription());
            }
        } else {
            respDTO.setResponse(invalidData.getCode(), invalidData.getDescription());
        }
    }
}
