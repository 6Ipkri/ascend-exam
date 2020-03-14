package com.ascendcorp.exam.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class InquiryServiceResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tranID;
    private String namespace;
    private String reasonCode;
    private String reasonDesc;
    private String balance;
    private String ref_no1;
    private String ref_no2;
    private String amount;
    private String accountName = null;

    public void setResponse(String code, String description) {
        this.reasonCode = code;
        this.reasonDesc = description;
    }

}
