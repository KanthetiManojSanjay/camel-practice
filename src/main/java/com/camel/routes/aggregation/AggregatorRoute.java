package com.camel.routes.aggregation;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

import static org.apache.camel.LoggingLevel.ERROR;

/**
 * @author kansanja on 24/12/21.
 */
@Component
@ConditionalOnProperty(name = "com.camel.aggregator.enabled", havingValue = "true")
public class AggregatorRoute extends RouteBuilder {

    private static final String CORRELATION_ID = "correlationId";

    @Override
    public void configure() throws Exception {
        from("timer:insurance?period=1000")
                .process(exchange -> {
                    Message message = exchange.getMessage();
                    message.setHeader(CORRELATION_ID, new Random().nextInt(4));
                    message.setBody(new Date().toString());
                })
                .aggregate(header(CORRELATION_ID), new MyAggregationStrategy())
                .completionSize(3)
                .log(ERROR, "${header." + CORRELATION_ID + "} ${body}");
    }
}
