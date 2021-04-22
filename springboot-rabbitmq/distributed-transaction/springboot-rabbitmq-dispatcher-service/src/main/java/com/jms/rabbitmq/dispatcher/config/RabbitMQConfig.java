package com.jms.rabbitmq.dispatcher.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于配置rabbitmq交换机和队列
 */
@Configuration
public class RabbitMQConfig {
    private static String EXCHANGE_NAME="order-fanout-exchange";
    private static String DEAD_EXCHANGE_NAME="order-dead-fanout-exchange";
    @Bean
    public FanoutExchange fanoutExchange(){
      return   new FanoutExchange(EXCHANGE_NAME);
    }
    @Bean
    public FanoutExchange deadFanoutExchange(){
        return   new FanoutExchange(DEAD_EXCHANGE_NAME);
    }
    @Bean
    public Queue orderDeadQueue(){
        return new Queue("order-dead-queue",true,false,false);
    }
    @Bean
    public Queue orderQueue(){
        Map<String,Object> map = new HashMap<>();
        //绑定死信队列
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
        return new Queue("order-queue",true,false,false,map);
    }
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue()).to(fanoutExchange());
    }
    @Bean
    public Binding orderDeadBinding(){
        return BindingBuilder.bind(orderDeadQueue()).to(deadFanoutExchange());
    }
}
