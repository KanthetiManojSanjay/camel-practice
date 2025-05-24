package com.camel.routes.jms;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author kansanja on 27/12/21.
 */
//@Component
public class JMSRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:orders")
                .log(LoggingLevel.INFO, "Got a message: ${body}");
    }
}
