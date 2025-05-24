package com.camel.processor;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author kansanja on 24/05/25.
 */
public class MaskSensitiveInfo implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String line = exchange.getIn().getBody(String.class);
        if (line.toLowerCase().startsWith("password:") || line.toLowerCase().startsWith("pwd:")) {
            line = line.substring(0, line.lastIndexOf(":")) + "*****";
            exchange.getIn().setBody(line);
        }
    }
}
