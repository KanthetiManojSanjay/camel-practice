package com.camel.routes.jms;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;

import javax.jms.ConnectionFactory;

/**
 * @author kansanja on 27/12/21.
 */
//@Configuration
public class JMSConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        // RMQConnectionFactory - for RabbitMQ connection
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
}
