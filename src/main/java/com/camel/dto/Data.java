package com.camel.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author kansanja on 09/02/22.
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private Metadata metadata;
    private Payment payment;
    private AccountingPostingResponse accountingPostingResponse;
}
