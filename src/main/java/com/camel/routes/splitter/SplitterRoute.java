package com.camel.routes.splitter;


import com.camel.dto.CustomerOrders;
import com.camel.dto.Order;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kansanja on 25/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.splitter.enabled", havingValue = "true")
public class SplitterRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //Simple split
        from("direct:start")
                .log("Before split ${body}")
                .split(body()).delimiter("#") // By default delimiter is comma which need not be specified explicitly
                .log("Split line ${body}")
                .to("mock:split");

        //Complex split
        from("direct:customerOrder")
                .log("CustomerId: ${body.customerId}")
                //Below are different approaches
//                        .split(simple("${body.orders}"))
//                        .split(method(OrderService.class))
                .split(method(OrderService.class, "getOrders"))
                .log("Order: ${body}");

        //Splitter & Aggregation together
        from("direct:customerOrderAggregate")
                .log("${body}")
                .split(body(), new WordAggregationStrategy()).stopOnException()
                .bean(WordTranslateBean.class).to("mock:split")
                .end()
                .log("Aggregated ${body}")
                .to("mock:aggregatedResult");
    }


    static class OrderService {
        public static List<Order> getOrders(CustomerOrders customerOrders) {
            return customerOrders.getOrders();

        }
    }

}
