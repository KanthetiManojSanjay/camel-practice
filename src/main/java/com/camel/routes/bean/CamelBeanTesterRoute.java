package com.camel.routes.bean;

import org.apache.camel.Handler;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kansanja on 24/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.bean-test.enabled", havingValue = "true")
public class CamelBeanTesterRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:myTimer?period=10000")
                .setBody(e -> new Date())
                //Bean component Approach
//                .to("bean:com.camel.routes.MyBean")
//                .to("bean:com.camel.routes.MyBean?method=fromServer")
                .setHeader("myHeader", () -> "check")
//                .setHeader(BEAN_METHOD_NAME, () -> "fromClient") // Takes preference at top
//                .to("bean:com.camel.routes.MyBean?method=fromServer(${body},${header.myHeader})")
                // Java DSL Approach
//                .bean("bean:com.camel.routes.MyBean")
//                .bean(new MyBean())
//                .bean(MyBean.class)
                .bean(MyBean.class, "fromServer(${body},${header.myHeader})")
                .log(LoggingLevel.INFO, "Message is : ${body}");

    }
}

class MyBean {
    public String fromClient(String msg) {
        return "From client: " + msg;
    }

    public String fromServer(String msg) {
        return "From server: " + msg;
    }

    public String fromServer(String body, String header) {
        return "From server: " + body + "; Header: " + header;
    }

    // If no method is specified in the bean component in route then this default method will be executed
    @Handler
    public String fromDefault(String msg) {
        return "From Default: " + msg;
    }

}
