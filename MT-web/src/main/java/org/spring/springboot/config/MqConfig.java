package org.spring.springboot.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Bean
    public Queue queue1(){
        return new Queue("MT-phone-upload");
    }

    @Bean
    public Queue queue2(){
        return new Queue("MT-phone");
    }

    @Bean
    public Queue queue3(){
        return new Queue("test1");
    }
}
