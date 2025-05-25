package com.camel.routes.multicast;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kansanja on 24/12/21.
 */

@Component
@ConditionalOnProperty(name = "com.camel.multicast.enabled", havingValue = "true")
public class MulticastRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        AtomicInteger orderId = new AtomicInteger(100);
        from("timer:orders?period=2000")
                .process(exchange -> exchange.getIn().setBody("{'orderId': '" + (orderId.incrementAndGet()) + "'," + "'price': '$20.00'}"))
                .multicast().parallelProcessing()
                .to("direct:payment", "direct:stock-allocation");

        from("direct:payment")
                .process(exchange -> enrich(exchange, "Paid"))
                .log(LoggingLevel.ERROR, "Payment: ${body}");

        from("direct:stock-allocation")
                .process(exchange -> enrich(exchange, "Allocated"))
                .log(LoggingLevel.ERROR, "Stock Allocation: ${body}");

    }

    private void enrich(Exchange exchange, String orderStatus) {
        Message in = exchange.getIn();
        String order = in.getBody(String.class);
        String status = "'Status':'" + orderStatus + "'";
        String body = order.replace("}", "," + status + "}");
        in.setBody(body);
    }
}
