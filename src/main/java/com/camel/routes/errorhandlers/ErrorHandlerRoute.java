package com.camel.routes.errorhandlers;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.camel.routes.errorhandlers.CommonErrorHandlerRoute.COUNTER;


/**
 * @author kansanja on 24/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.error-handlers1.enabled", havingValue = "true")
public class ErrorHandlerRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        errorHandler(
                deadLetterChannel("direct:errorHandler")
                        .maximumRedeliveries(2)
        );

        from("timer:time?period=1000")
                .process(exchange -> exchange.getIn().setBody(new Date()))
                .choice()
                    .when(e -> COUNTER.incrementAndGet() % 2 == 0)
                        .bean(HelloBean.class, "callBad")
                    .otherwise()
                        .bean(HelloBean.class, "callGood")
                    .end()
                .log(LoggingLevel.ERROR, ">>${header.firedTime} >> ${body}")
                .to("log:reply");


    }
}
