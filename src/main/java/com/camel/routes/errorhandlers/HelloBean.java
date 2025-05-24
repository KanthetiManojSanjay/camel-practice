package com.camel.routes.errorhandlers;

import java.util.Date;

import static com.camel.routes.errorhandlers.CommonErrorHandlerRoute.COUNTER;

/**
 * @author kansanja on 24/05/25.
 */
public class HelloBean {
    public HelloBean() {
        System.out.println("HelloBean Constructor");
    }

    public String callGood() {
        System.out.println("Good Call for " + COUNTER.get());
        return "Good:" + new Date();
    }

    public String callBad() {
        System.out.println("Bad Call for " + COUNTER.get());
        throw new RuntimeException("Exception for " + COUNTER.get());
    }
}
