package com.camel.processor;

import com.camel.dto.OutboundPayment;
import com.camel.entity.EventData;
import com.camel.repo.EventRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

/**
 * @author kansanja on 09/02/22.
 */
//@Component
@Slf4j
public class OutboundPaymentProcessor implements Processor {

    @Autowired
    EventRepo eventRepo;

    @Override
    public void process(Exchange exchange) throws Exception {
        OutboundPayment outboundPayment = exchange.getMessage().getBody(OutboundPayment.class);

        EventData eventData=new EventData();
        eventData.setCorrelationId(outboundPayment.getData().getPayment().getId());
        eventData.setEventName(outboundPayment.getEventName());
        eventData.setTimestamp(outboundPayment.getTimestamp());
        eventData.setProcessingDate(outboundPayment.getData().getPayment().getProcessingDate());
//        eventData.setSettlementDate();
//        eventData.setSettlementCycleId();
        eventData.setSettlementAmount(outboundPayment.getData().getPayment().getAmount());
        eventData.setSettlementCurrency(outboundPayment.getData().getPayment().getCurrency());
        if(outboundPayment.getData().getMetadata()!=null){
            eventData.setInitiatingChannel(outboundPayment.getData().getMetadata().getInitiatingChannel());
            eventData.setPostingPlatform(outboundPayment.getData().getMetadata().getPostingPlatform());
            eventData.setPaymentDirection(outboundPayment.getData().getMetadata().getPaymentJourney());
        }
        String debtorIdentification = outboundPayment.getData().getPayment().getDebtorAccount().getIdentification();
        eventData.setOriginatorSortCode(debtorIdentification.substring(0,7));
        eventData.setOriginatorAccountNumber(debtorIdentification);
        eventData.setOriginatorAccountName(outboundPayment.getData().getPayment().getDebtorAccount().getAccountName());

        String creditorIdentification = outboundPayment.getData().getPayment().getCreditorAccount().getIdentification();
        eventData.setBeneficiarySortCode(creditorIdentification.substring(0,7));
        eventData.setBeneficiaryAccountNumber(creditorIdentification);
        eventData.setBeneficiaryAccountName(outboundPayment.getData().getPayment().getCreditorAccount().getAccountName());
        if(outboundPayment.getData().getPayment().getFpid()!=null)
            eventData.setFpId(outboundPayment.getData().getPayment().getFpid());
        eventData.setTransactionReferenceNumber(outboundPayment.getData().getPayment().getTransactionReferenceNumber());

        eventData.setEndToEndReference(outboundPayment.getData().getPayment().getEndToEndReference());
//        eventData.setOriginalPaymentId();

        eventData.setPaymentScheme(outboundPayment.getData().getPayment().getPaymentScheme());
        eventData.setSchemePaymentType(outboundPayment.getData().getPayment().getSchemePaymentType());
        eventData.setSchemePaymentSubType(outboundPayment.getData().getPayment().getSchemePaymentSubType());

        if(outboundPayment.getData().getPayment().getPaymentPurpose()!=null)
            eventData.setPaymentPurpose(outboundPayment.getData().getPayment().getPaymentPurpose());
        eventData.setOriginatorReference(outboundPayment.getData().getPayment().getReference());
        eventData.setRemittanceInformation(outboundPayment.getData().getPayment().getRemittanceInformation());
        eventData.setIsPOO(false);
        eventData.setPaymentStatus(outboundPayment.getResult());
        if(!outboundPayment.getResult().toUpperCase().equals("SUCCESS")){
            eventData.setPaymentStatusReason(outboundPayment.getErrors().get(0).getDescription());
            eventData.setPaymentStatusCode(outboundPayment.getErrors().get(0).getCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(outboundPayment);
        Blob origBlob= new SerialBlob(payload.getBytes());
        eventData.setPayload(origBlob);
        log.info("");

//        eventData.setSchemeStatusCode();
//        eventData.setSchemeSettlementDate();
//        eventData.setSchemeSettlementCycle();
//        eventData.setInitialSanctionResponse();
//        eventData.setFinalSanctionResponse();

        eventRepo.save(eventData);
        Message message = new DefaultMessage(exchange.getContext());
        message.setBody(eventData);
        exchange.setMessage(message);
    }
}
