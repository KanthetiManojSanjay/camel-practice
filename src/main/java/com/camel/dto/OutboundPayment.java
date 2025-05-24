package com.camel.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kansanja on 09/02/22.
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundPayment {
    private String eventSource;
    private String eventName;
    private Data data;
    private String result;
    private String timestamp;
    private List<Errors> errors;
    private String eventType;
    private String version;
}
