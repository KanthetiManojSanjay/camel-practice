package com.camel.routes.kafka;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import java.util.Date;

/**
 * @author kansanja on 08/02/22.
 */
//@Component
public class Kafka extends RouteBuilder {

    final String KAFKA_ENDPOINT = "kafka:%s?brokers=localhost:9092";

    @Override
    public void configure() throws Exception {
        fromF(KAFKA_ENDPOINT, "stock-live")
                .log(LoggingLevel.ERROR, "[${header.kafka.OFFSET}] [${body}]")
                .bean(StockPriceEnricher.class)
                .log(LoggingLevel.ERROR, "[${body}]")
                .toF(KAFKA_ENDPOINT, "stock-audit");


    }

    private class StockPriceEnricher {
        public String enrichStockPrice(String stockPrice) {
            return stockPrice + "," + new Date();
        }

    }
}
