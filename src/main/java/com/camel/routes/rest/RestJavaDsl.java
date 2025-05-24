package com.camel.routes.rest;

import com.camel.dto.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

/**
 * @author kansanja on 23/12/21.
 */

//@Component
public class RestJavaDsl extends RouteBuilder {


    private final WeatherDataProvider weatherDataProvider;

    public RestJavaDsl(WeatherDataProvider weatherDataProvider) {
        this.weatherDataProvider = weatherDataProvider;
    }

    @Override
    public void configure() throws Exception {
        from("rest:get:javadsl/weather/{city}?produces=application/json")
                .outputType(WeatherDto.class)
                .process(this::getWeatherData);
    }

    private void getWeatherData(Exchange exchange) {
        String city = exchange.getMessage().getHeader("city", String.class);
        WeatherDto weatherDto = weatherDataProvider.getCurrentWeather(city);

        if (Objects.nonNull(weatherDto)) {
            Message message = new DefaultMessage(exchange.getContext());
            message.setBody(weatherDto);
            exchange.setMessage(message);
        } else {
            exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, HttpStatus.NOT_FOUND.value());
        }


    }
}
