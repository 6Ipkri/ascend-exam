package com.ascendcorp.exam.service;

import com.ascendcorp.exam.model.*;
import com.ascendcorp.exam.proxy.BankProxyGateway;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.WebServiceException;


public class InquiryService {

    @Autowired
    private BankProxyGateway bankProxyGateway;

    final static Logger log = Logger.getLogger(InquiryService.class);
    public InquiryServiceResultDTO inquiry(Transaction transaction,
                                           Bank bank,
                                           double amount,
                                           Reference reference
                                          ) {
        InquiryServiceResultDTO respDTO = null;
        try {
            checkInputNotNull( transaction,bank, amount, reference);
            TransferResponse response = bankProxyGateway.requestTransfer(transaction,bank,amount,reference);

            if (response != null)
            {
                respDTO = new InquiryServiceResultDTO();

                respDTO.setRef_no1(response.getReferenceCode1());
                respDTO.setRef_no2(response.getReferenceCode2());
                respDTO.setAmount(response.getBalance());
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

                setResponse(respDTO, "500", "General Invalid Data");
            }
        } catch (WebServiceException r) {
            String faultString = r.getMessage();
            if (respDTO == null) {
                respDTO = new InquiryServiceResultDTO();
                if (faultString != null && (faultString.contains("java.net.SocketTimeoutException")
                        || faultString.contains("Connection timed out"))) {
                    setResponse(respDTO, "503", "Error timeout");
                } else {
                    setResponse(respDTO, "504", "Internal Application Error");

                }
            }
        } catch (Exception e) {
            if (respDTO == null || (respDTO != null && respDTO.getReasonCode() == null)) {
                respDTO = new InquiryServiceResultDTO();
                setResponse(respDTO, "504", "Internal Application Error");
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
        setResponse(respDTO, "200", response.getDescription());
        respDTO.setAccountName(response.getDescription());
    }

    private void responseInvalidData(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 3) {
                setResponse(respDTO, respDesc[1], respDesc[2]);
            } else {
                setResponse(respDTO, "400", "General Invalid Data");
            }
        } else {
            setResponse(respDTO, "400", "General Invalid Data");
        }
    }

    private void responseTransactionError(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        String subIdx1, subIdx2, subIdx3;
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            subIdx1 = respDesc[0];

            if (respDesc.length == 1) {
                setResponse(respDTO, "500", "General Transaction Error");
            } else if (respDesc.length == 2) {
                subIdx2 = respDesc[1];
                log.debug(subIdx1);
                setResponse(respDTO, subIdx1, subIdx2);
            } else {
                subIdx2 = respDesc[1];
                subIdx3 = respDesc[2];

                setResponse(respDTO, subIdx2, subIdx3);
            }
        } else {
            setResponse(respDTO, "500", "General Transaction Error");
        }
    }

    private void responseUnknown(InquiryServiceResultDTO respDTO, TransferResponse response) {
        String replyDesc = response.getDescription();
        if (replyDesc != null) {
            String[] respDesc = replyDesc.split(":");
            if (respDesc.length >= 2) {
                setResponse(respDTO, respDesc[0], respDesc[1]);
                if (respDTO.getReasonDesc() == null || respDTO.getReasonDesc().trim().length() == 0) {
                    respDTO.setReasonDesc("General Invalid Data");
                }
            } else {
                setResponse(respDTO, "501", "General Invalid Data");
            }
        } else {
            setResponse(respDTO, "501", "General Invalid Data");
        }
    }

    private void setResponse(InquiryServiceResultDTO respDTO, String code, String description) {
        respDTO.setReasonCode(code);
        respDTO.setReasonDesc(description);
    }
}
