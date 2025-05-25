package com.camel.routes.rabbitmq;

import com.camel.dto.WeatherDto;
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
@ConditionalOnProperty(name = "com.camel.rabbitmq.enabled", havingValue = "true")
public class WeatherRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // componentName:exchangeName?queue=QueueName&routingKey(routingkey sits b/w exchange & queue)

        from("rabbitmq:amq.direct?queue=weather&routingKey=weather&autoDelete=false")
                .log(LoggingLevel.ERROR, "Before Enrichment: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .process(this::enrichWeatherDto)
                .log(LoggingLevel.ERROR, "After Enrichment: ${body}")
                .marshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .to("rabbitmq:amq.direct?queue=weather-event&routingKey=weather-event&autoDelete=false")
                .to("file:///Users/kansanja/Documents/?fileName=weather-events.txt&fileExist=Append");
    }

    private void enrichWeatherDto(Exchange exchange) {
        WeatherDto weatherDto = exchange.getMessage().getBody(WeatherDto.class);
        weatherDto.setReceivedTime(new Date().toString());

        Message message = new DefaultMessage(exchange.getContext());
        message.setBody(weatherDto);
        exchange.setMessage(message);
    }
}
