package com.camel.routes.errorhandlers;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.apache.camel.LoggingLevel.ERROR;

/**
 * @author kansanja on 24/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.error-handlers1.enabled", havingValue = "true")
public class TryCatchRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("timer:time?period=1000")
                .process(exchange -> exchange.getIn().setBody(new Date()))
                .doTry()
                    .bean(HelloBean.class, "callBad")
                .doCatch(Exception.class)
                    .to("direct:exceptionHandler")
                .end()
                .log(ERROR, ">>${header.firedTime} >> ${body}")
                .to("log:reply");


    }
}
