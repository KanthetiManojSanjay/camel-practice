package com.camel.routes;

import lombok.Builder;
import lombok.Data;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kansanja on 24/05/25.
 */

/**
 * Splitter - Through this pattern we can split Collection/List/Set/Map/Array/Iterator/Iterable/NodeList(xml)/String
 */
public class SplitterRouteTest extends CamelTestSupport {

    @Test
    void splitEip() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:split");
        mock.expectedBodiesReceived("A", "B", "C");
//        List<String> body = Arrays.asList("A", "B", "C");
        template.sendBody("direct:start", "A#B#C");
        assertMockEndpointsSatisfied();
    }

    @Test
    void complexSplitEip() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().orderId("0100").items(Arrays.asList("100", "101", "102")).build());
        orders.add(Order.builder().orderId("0200").items(Arrays.asList("200", "201", "202")).build());
        CustomerOrders customerOrders = CustomerOrders.builder().customerId("John").orders(orders).build();
        template.sendBody("direct:customerOrder", customerOrders);
    }


    @Test
    void splitAndAggregateEip() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:aggregatedResult");
        mock.expectedBodiesReceived("A=Apple + B=Ball + C=Cat");
        template.sendBody("direct:customerOrderAggregate", "A,B,C");
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
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
        };
    }

    static class OrderService {
        public static List<Order> getOrders(CustomerOrders customerOrders) {
            return customerOrders.getOrders();

        }
    }

    @Data
    @Builder
    static class CustomerOrders {
        private String customerId;
        private List<Order> orders;
    }


    @Data
    @Builder
    static class Order {
        private String orderId;
        private List<String> items;
    }
}
