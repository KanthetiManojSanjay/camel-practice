package com.camel.routes.wiretap;

import com.camel.dto.TransactionDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.DefaultMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kansanja on 24/12/21.
 */
@Component
@ConditionalOnProperty(name = "com.camel.wiretap.enabled", havingValue = "true")
public class Wiretap extends RouteBuilder {
    private static final String RABBIT_URI = "rabbitmq:amq.direct?queue=%s&routingKey=%s&autoDelete=false";
    private static final String RECEIVER = "receiver";
    private static final String SENDER = "sender";
    private static final String AUDIT = "audit-transactions";
    private static final String AUDIT_TRANSACTION_ROUTE = "direct:audit-transaction";

    @Override
    public void configure() throws Exception {

        fromF(RABBIT_URI, SENDER, SENDER)
                .unmarshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .wireTap(AUDIT_TRANSACTION_ROUTE)
                .process(this::enrichTransaction)
                .marshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .toF(RABBIT_URI, RECEIVER, RECEIVER)
                .log(LoggingLevel.ERROR, "Money transferred: ${body}");

        from(AUDIT_TRANSACTION_ROUTE)
                .process(this::enrichTransaction)
                .marshal().json(JsonLibrary.Jackson, TransactionDto.class)
                .toF(RABBIT_URI, AUDIT, AUDIT);
    }


    private void enrichTransaction(Exchange exchange) {
        TransactionDto dto = exchange.getMessage(TransactionDto.class);
        dto.setTransactionDate(new Date().toString());

        Message message = new DefaultMessage(exchange.getContext());
        message.setBody(dto);
        exchange.setMessage(message);
    }
}
