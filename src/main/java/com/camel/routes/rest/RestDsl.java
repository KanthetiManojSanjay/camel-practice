package com.camel.routes.rest;

import com.camel.dto.WeatherDto;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.camel.config.CamelConfiguration.RABBIT_URI;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.model.rest.RestParamType.body;


/**
 * @author kansanja on 23/12/21.
 */
@Component
public class RestDsl extends RouteBuilder {
    private final WeatherDataProvider weatherDataProvider;

    public RestDsl(WeatherDataProvider weatherDataProvider) {
        this.weatherDataProvider = weatherDataProvider;
    }

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto)
                .dataFormatProperty("prettyPrint", "true")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Camel Rest APIs")
                .apiProperty("api.version", "1.0")
                .apiContextListing(true);
        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/weather/{city}")
                .responseMessage("200", "On good Request")
                .responseMessage("404", "For ivalid requests")
                .description("Get weather data for given city")
                .param()
                    .name("city").type(RestParamType.path).description("The name of the city e.g: London").dataType("string")
                .endParam()
                .outType(WeatherDto.class).to("direct:get-weather-data")
                .post("/weather").responseMessage("201", "When Created").description("Add Weather for a city").type(WeatherDto.class)
                .param()
                    .name("body").type(body).description("Payload for Weather")
                .endParam()
                .to("direct:save-weather-data")
        ;

        from("direct:save-weather-data")
                .routeId("saveWeatherData")
                .log(LoggingLevel.INFO,"saveWeatherData ${body}")
                .process(this::saveWeatherDataAndSetToExchange);

        from("direct:get-weather-data")
                .routeId("getWeatherData")
                .log(LoggingLevel.INFO,"getWeatherData for city: ${header.city}")
                .process(this::getWeatherData);
//                .wireTap("direct:write-to-rabbit");

        from("direct:write-to-rabbit")
                .marshal().json(JsonLibrary.Jackson, WeatherDto.class)
                .toF(RABBIT_URI, "weather-event", "weather-event");

    }

    private void saveWeatherDataAndSetToExchange(Exchange exchange) {
        WeatherDto dto = exchange.getMessage().getBody(WeatherDto.class);
        weatherDataProvider.setCurrentWeather(dto);

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
