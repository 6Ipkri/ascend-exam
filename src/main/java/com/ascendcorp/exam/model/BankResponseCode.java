package com.ascendcorp.exam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum  BankResponseCode {

    RE_200_APPROVED("200" , "approved"),
    RE_501_GENERAL_INVALID_DATA("501" , "General Invalid Data"),
    RE_500_GENERAL_TRANSACTION("500" , "General Transaction Error"),
    RE_503_ERROR_TIME_OUT("503" , "Error timeout"),
    RE_504_INTERNAL_ERROR("504","Internal Application Error"),

    TR_APPROVED("approved" , "approved"),
    TR_INVALID_DATA_100_1091("invalid_data" , "100:1091:Data type is invalid."),
    TR_INVALID_DATA_NO_DESC("invalid_data" , "General error."),
    TR_NOT_SUPPORT("not_support" , "Not Support"),
    TR_TRANSACTION_ERROR_NULL_DESC("transaction_error" , null),
    TR_TRANSACTION_ERROR_NO_DESC("transaction_error" , "Transaction error."),
    TR_TRANSACTION_ERROR_100_1091("transaction_error" , "100:1091:Transaction is error with code 1091."),
    TR_TRANSACTION_ERROR_1092("transaction_error" , "1092:Transaction is error with code 1092."),
    TR_TRANSACTION_ERROR_98("transaction_error","98:Transaction is error with code 98."),
    TR_UNKNOWN_NULL_DESC("unknown" , null),
    TR_UNKNOWN_5001("unknown" , "5001:Unknown error code 5001"),
    TR_UNKNOWN_5002("unknown" , "5002: "),
    TR_UNKNOWN_501("unknown" , "General Invalid Data code 501");

    @Getter
    private String code;
    @Getter
    private String description;

}
