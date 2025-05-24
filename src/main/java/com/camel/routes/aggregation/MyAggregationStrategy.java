package com.camel.routes.aggregation;

import org.apache.camel.Exchange;

import java.util.Objects;

/**
 * @author kansanja on 24/12/21.
 */
public class MyAggregationStrategy implements org.apache.camel.AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (Objects.isNull(oldExchange)) {
            return newExchange;
        }
        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);

        String aggregateBody = oldBody + "->" + newBody;

        oldExchange.getIn().setBody(aggregateBody);
        
        return oldExchange;
    }
}
