package com.camel.routes.errorhandlers;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.camel.LoggingLevel.WARN;

/**
 * @author kansanja on 24/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.error-handlers.enabled", havingValue = "true")
public class CommonErrorHandlerRoute extends RouteBuilder {

    final static AtomicInteger COUNTER = new AtomicInteger(1);

    @Override
    public void configure() throws Exception {
        from("direct:errorHandler")
                .log(WARN, "In Exception Handler")
//                .process(e -> SECONDS.sleep(5))
                .log(WARN, "${body}");
    }
}
