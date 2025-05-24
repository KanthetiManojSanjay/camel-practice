package com.camel.routes.kafka;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author kansanja on 14/05/23.
 */
//@Component
public class KafkaProducer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://test-kafka?period=10000")
                .setBody(simple("welcome to kafka"))
                .multicast().parallelProcessing()
                .to("kafka:stock-live1?brokers=localhost:9092",
                        "kafka:stock-live2?brokers=localhost:9092",
                        "kafka:stock-live3?brokers=localhost:9092",
                        "kafka:stock-live4?brokers=localhost:9092",
                        "kafka:stock-live5?brokers=localhost:9092",
                        "kafka:stock-live6?brokers=localhost:9092",
                        "kafka:stock-live7?brokers=localhost:9092",
                        "kafka:stock-live8?brokers=localhost:9092",
                        "kafka:stock-live9?brokers=localhost:9092",
                        "kafka:stock-live10?brokers=localhost:9092"
                );
    }
}
