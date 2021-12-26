package com.camel.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

/**
 * @author kansanja on 24/12/21.
 */
public class HelloRouteJunitMockTest extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:greeting").to("mock:greetingResult");
            }
        };
    }

    @Test
    public void testMocksAreValid() throws InterruptedException {

        MockEndpoint mock = getMockEndpoint("mock:greetingResult");
        mock.expectedMessageCount(2);

        System.out.println("Sending 1");
        template.sendBody("direct:greeting", "Team");

        System.out.println("Sending 2");
        template.sendBody("direct:greeting", "Me");

        mock.assertIsSatisfied();
    }
}
