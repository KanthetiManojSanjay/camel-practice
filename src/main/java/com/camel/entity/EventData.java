package com.camel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * @author kansanja on 09/02/22.
 */
@Entity
@Table(name = "metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(EventDataPK.class)
public class EventData {
    @Id
    private String correlationId;
    @Id
    private String eventName;
    private String timestamp;
    private String processingDate;
    private String settlementDate;
    private Integer settlementCycleId;
    private String settlementAmount;
    private String settlementCurrency;
    private String initiatingChannel;
    private String postingPlatform;
    private String paymentDirection;
    private String originatorSortCode;
    private String originatorAccountNumber;
    private String originatorAccountName;
    private String beneficiarySortCode;
    private String beneficiaryAccountNumber;
    private String beneficiaryAccountName;
    private String fpId;
    private String transactionReferenceNumber;
    private String endToEndReference;
    private String originalPaymentId;
    private String paymentScheme;
    private String schemePaymentType;
    private String schemePaymentSubType;
    private String paymentPurpose;
    private String originatorReference;
    private String remittanceInformation;
    private Boolean isPOO;
    private String paymentStatus;
    private String paymentStatusReason;
    private String paymentStatusCode;
    private String schemeStatusCode;
    private String schemeSettlementDate;
    private String schemeSettlementCycle;
    private String initialSanctionResponse;
    private String finalSanctionResponse;
    private Blob payload;
}
