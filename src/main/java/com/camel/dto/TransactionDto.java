package com.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kansanja on 24/12/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {
    private String transactionId;
    private String senderAccountId;
    private String receiverAccountId;
    private String amount;
    private String currency;
    private String transactionDate;
}
