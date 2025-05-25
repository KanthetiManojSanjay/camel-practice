package com.camel.routes.contentbasedrouter;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author kansanja on 24/12/21.
 */
@Component
@ConditionalOnProperty(name = "com.camel.choice.enabled", havingValue = "true")
public class ChoiceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:orders")
                .choice()
                .when(simple("${header.inventory}== 'widget'"))
                    .to("direct:widget")
                .when(simple("${header.inventory}== 'gadget'"))
                    .to("direct:gadget")
                .otherwise()
                    .to("direct:general");

        from("direct:widget").routeId("widget")
                .log(LoggingLevel.ERROR, "Got a widget order for ${body}");

        from("direct:gadget").routeId("gadget")
                .log(LoggingLevel.ERROR, "Got a gadget order for ${body}");

        from("direct:general").routeId("general")
                .log(LoggingLevel.ERROR, "Got a general order for ${body}");


    }
}
