package com.camel.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author kansanja on 25/05/25.
 */
@Data
@Builder
public class Order {
    private String orderId;
    private List<String> items;
}
