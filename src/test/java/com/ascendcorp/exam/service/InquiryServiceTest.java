package com.ascendcorp.exam.service;

import com.ascendcorp.exam.model.*;
import com.ascendcorp.exam.proxy.BankProxyGateway;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.ws.WebServiceException;
import java.sql.SQLException;
import java.util.Date;

import static junit.framework.TestCase.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class InquiryServiceTest {

    @InjectMocks
    InquiryService inquiryService;

    @Mock
    BankProxyGateway bankProxyGateway;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void should_return_500_when_no_require_value() throws SQLException {

        Transaction transaction = new Transaction(null, new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt", null, null);

        // Transaction Id
        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction,
                bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());

        // Datetime
        transaction = new Transaction("1234", null,
                "Mobile", null);
        inquiry = inquiryService.inquiry(transaction,
                bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());

        // Channel
        transaction = new Transaction("1234", new Date(),
                null, null);
        inquiry = inquiryService.inquiry(transaction,
                bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());

        // BankCode
        transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        bank = new Bank(null, "4321000");
        inquiry = inquiryService.inquiry(transaction,
                bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());

        // BankNumber
        bank = new Bank("BANK1", null);
        inquiry = inquiryService.inquiry(transaction,
                bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());

        // Amount
        bank = new Bank("BANK1", "4321000");
        inquiry = inquiryService.inquiry(transaction,
                bank, 0d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }


    @Test
    public void should_return_200_when_bank_approved() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("approved");
        transferResponse.setDescription("approved");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("200", inquiry.getReasonCode());
        assertEquals("approved", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_invalid_data_without_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("invalid_data");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);


        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("400", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }


    @Test
    public void should_return_1091_with_reason_desc_when_invalid_data_with_desc_and_code() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("invalid_data");
        transferResponse.setDescription("100:1091:Data type is invalid.");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("1091", inquiry.getReasonCode());
        assertEquals("Data type is invalid.", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_invalid_data_with_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("invalid_data");
        transferResponse.setDescription("General error.");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("400", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_error_and_desc_is_null() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("transaction_error");


        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Transaction Error", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_error_and_no_desc_code() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("transaction_error");
        transferResponse.setDescription("Transaction error.");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("500", inquiry.getReasonCode());
        assertEquals("General Transaction Error", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_error_and_desc_3_code() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("transaction_error");
        transferResponse.setDescription("100:1091:Transaction is error with code 1091.");
        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("1091", inquiry.getReasonCode());
        assertEquals("Transaction is error with code 1091.", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_error_and_desc_2_code() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("transaction_error");
        transferResponse.setDescription("1092:Transaction is error with code 1092.");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("1092", inquiry.getReasonCode());
        assertEquals("Transaction is error with code 1092.", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_400_when_error_and_desc_code_98() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("transaction_error");
        transferResponse.setDescription("98:Transaction is error with code 98.");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("98", inquiry.getReasonCode());
        assertEquals("Transaction is error with code 98.", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_501_when_unknown_and_without_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("unknown");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("501", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_501_when_unknown_and_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("unknown");
        transferResponse.setDescription("5001:Unknown error code 5001");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("5001", inquiry.getReasonCode());
        assertEquals("Unknown error code 5001", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_501_when_unknown_and_empty_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("unknown");
        transferResponse.setDescription("5002: ");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("5002", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_501_when_unknown_and_text_desc() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("unknown");
        transferResponse.setDescription("General Invalid Data code 501");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("501", inquiry.getReasonCode());
        assertEquals("General Invalid Data", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_504_when_error_desc_not_support() throws SQLException {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setResponseCode("not_support");
        transferResponse.setDescription("Not support");

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(transferResponse);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("504", inquiry.getReasonCode());
        assertEquals("Internal Application Error", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_504_when_response_null() throws SQLException {

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenReturn(null);

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);
        assertNotNull(inquiry);
        assertEquals("504", inquiry.getReasonCode());
        assertEquals("Internal Application Error", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_503_when_throw_web_service_exception() throws SQLException {

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenThrow(WebServiceException.class);


        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("504", inquiry.getReasonCode());
        assertEquals("Internal Application Error", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_503_when_socket_timeout() throws SQLException {

        WebServiceException ex = new WebServiceException("java.net.SocketTimeoutException error");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenThrow(ex);

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("503", inquiry.getReasonCode());
        assertEquals("Error timeout", inquiry.getReasonDesc());
    }

    @Test
    public void should_return_503_when_connection_timeout() throws SQLException {

        WebServiceException ex = new WebServiceException("Server Connection timed out");

        when(bankProxyGateway.requestTransfer(any(Transaction.class) , any(Bank.class),anyDouble() , any(Reference.class))).thenThrow(ex);

        Transaction transaction = new Transaction("1234", new Date(),
                "Mobile", null);
        Bank bank = new Bank("BANK1", "4321000");
        Reference reference = new Reference("rrivsffv234c",
                "11223xfgt");

        InquiryServiceResultDTO inquiry = inquiryService.inquiry(transaction, bank, 100d, reference);

        assertNotNull(inquiry);
        assertEquals("503", inquiry.getReasonCode());
        assertEquals("Error timeout", inquiry.getReasonDesc());
    }


}
