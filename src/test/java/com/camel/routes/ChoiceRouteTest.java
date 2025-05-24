package com.camel.routes;

import com.camel.routes.contentbasedrouter.ChoiceRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.apache.camel.builder.AdviceWith.adviceWith;

/**
 * @author kansanja on 24/12/21.
 */
public class ChoiceRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ChoiceRoute();
    }

    @Test
    public void givenGadgetOrderRequest_route_WillProcessGadgetOrder() throws Exception {
        MockEndpoint mockGadget = mockEndpoint("gadget", 1);
        MockEndpoint mockWidget = mockEndpoint("widget", 0);
        MockEndpoint mockGeneral = mockEndpoint("general", 0);

        context.start();
        String body = "Airpods";
        Map<String, Object> headers = Maps.newHashMap("inventory", "gadget");
        template.sendBodyAndHeaders("direct:orders", body, headers);
        assertAllSatisfied(mockGadget, mockWidget, mockGeneral);
    }

    @Test
    public void givenWidgetOrderRequest_route_WillProcessWidgetOrder() throws Exception {


        MockEndpoint mockGadget = mockEndpoint("gadget", 0);
        MockEndpoint mockWidget = mockEndpoint("widget", 1);
        MockEndpoint mockGeneral = mockEndpoint("general", 0);

        context.start();

        String body = "Amazon";
        Map<String, Object> headers = Maps.newHashMap("inventory", "widget");
        template.sendBodyAndHeaders("direct:orders", body, headers);

        assertAllSatisfied(mockGadget, mockWidget, mockGeneral);
    }

    @Test
    void givenGeneralOrderRequest_route_WillProcessGeneralOrder() throws Exception {
        MockEndpoint mockGadget = mockEndpoint("gadget", 0);
        MockEndpoint mockWidget = mockEndpoint("widget", 0);
        MockEndpoint mockGeneral = mockEndpoint("general", 1);

        context.start();

        String body = "T-Shirt";
        Map<String, Object> headers = Maps.newHashMap("inventory", "general");
        template.sendBodyAndHeaders("direct:orders", body, headers);

        assertAllSatisfied(mockGadget, mockWidget, mockGeneral);
    }

    private void assertAllSatisfied(MockEndpoint... mockEndPoint) throws InterruptedException {
        for (MockEndpoint endpoint : mockEndPoint) {
            endpoint.assertIsSatisfied();
        }
    }

    private MockEndpoint mockEndpoint(String orderType, int expectedCount) throws Exception {
        RouteDefinition route = context.getRouteDefinition(orderType);
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to("mock:" + orderType);
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:" + orderType);
        mockEndpoint.expectedMessageCount(expectedCount);
        return mockEndpoint;

    }
}
