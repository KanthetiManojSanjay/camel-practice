package com.camel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.camel"})
public class CamelPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelPracticeApplication.class, args);
    }

}
