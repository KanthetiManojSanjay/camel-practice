package com.camel.routes.rest;

import com.camel.dto.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.RoutePolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kansanja on 25/05/25.
 */
@Component
@ConditionalOnProperty(name = "com.camel.consume-rest.enabled", havingValue = "true")
public class ConsumeRestApi extends RouteBuilder {

    JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(WeatherDto.class);

    @Override
    public void configure() throws Exception {

        RoutePolicy dependentRoutePolicy = new DependentRoutePolicy("postWeatherData", "consumeWeatherData");

        from("timer:mytimer2?repeatCount=1&delay=5000")
                .autoStartup(false)
                .routeId("consumeWeatherData")
                .routePolicy(dependentRoutePolicy)
                .setHeader(Exchange.HTTP_METHOD, simple("GET"))
                .to("http://localhost:8081/services/weather/hyderabad")
                .log("consumeWeatherData: ${body}");

        from("timer:mytimer1?repeatCount=1")
                .autoStartup(false)
                .routeId("postWeatherData")
                .routePolicy(dependentRoutePolicy)
                .process(exchange -> {
                    WeatherDto dto = WeatherDto.builder().id(2).city("Hyderabad").temp("30").unit("C").receivedTime(new Date().toString()).build();
                    exchange.getIn().setBody(dto);
                })
                .marshal(jacksonDataFormat)
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("http://localhost:8081/services/weather")
                .log("postWeatherData: ${body}");

        from("timer:startBatchRoute?repeatCount=1&delay=1000")
                .routeId("batchStartRouteId")
                .to("controlbus:route?routeId=postWeatherData&action=start")
                .to("controlbus:route?routeId=postWeatherData&action=status")
                .to("controlbus:route?routeId=consumeWeatherData&action=status");

    }
}
