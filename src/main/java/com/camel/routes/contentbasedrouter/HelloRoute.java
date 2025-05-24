package com.camel.routes.contentbasedrouter;

import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.LoggingLevel.ERROR;

/**
 * @author kansanja on 24/12/21.
 */
//@Component
public class HelloRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:greeting").id("greeting")
                .log(ERROR,"Hello ${body}")
                .choice()
                .when().simple("${body} contains 'Team'")
                    .log(ERROR, "I like working in teams")
                .otherwise()
                    .log(ERROR, "Solo fighter")
                .end()
                .to("direct:finishGreeting");

        from("direct:finishGreeting")
                .log(ERROR, "Bye ${body}");
    }
}
