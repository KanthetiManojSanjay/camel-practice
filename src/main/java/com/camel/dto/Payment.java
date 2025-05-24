package com.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kansanja on 09/02/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String id;
    private String processingDate;
    private String currency;
    private String amount;
    private String reference;
    private String numericReference;
    private String transactionReferenceNumber;
    private Account debtorAccount;
    private Account creditorAccount;
    private String schemePaymentType;
    private String schemePaymentSubType;
    private String endToEndReference;
    private String regulatoryReporting;
    private String remittanceInformation;
    private String paymentType;
    private String paymentPurpose;
    private String paymentScheme;
    private String fpid;
}
