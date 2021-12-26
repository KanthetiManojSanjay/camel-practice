package com.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author kansanja on 24/12/21.
 */
@Component
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:greeting").id("greeting")
                .choice()
                .when().simple("${body} contains 'Team'")
                .log(LoggingLevel.ERROR, "I like working in teams")
                .otherwise()
                .log(LoggingLevel.ERROR, "Solo fighter")
                .end()
                .to("direct:finishGreeting");
        from("direct:finishGreeting")
                .log(LoggingLevel.ERROR, "Bye ${body}");
    }
}
