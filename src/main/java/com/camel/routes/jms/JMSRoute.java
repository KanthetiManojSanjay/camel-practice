package com.camel.routes.jms;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author kansanja on 27/12/21.
 */
@Component
@ConditionalOnProperty(name = "com.camel.jms.enabled", havingValue = "true")
public class JMSRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:orders")
                .log(LoggingLevel.INFO, "Got a message: ${body}");
    }
}
