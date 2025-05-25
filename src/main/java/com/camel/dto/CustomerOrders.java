package com.camel.dto;

import com.camel.routes.splitter.SplitterRoute;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author kansanja on 25/05/25.
 */
@Data
@Builder
public class CustomerOrders {
    private String customerId;
    private List<Order> orders;
}
