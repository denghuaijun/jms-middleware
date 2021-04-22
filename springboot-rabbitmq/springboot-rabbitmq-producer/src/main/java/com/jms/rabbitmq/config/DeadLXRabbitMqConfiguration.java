package com.jms.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置死信交换机及队列用来作为死信队列的容错
 */
@Configuration
public class DeadLXRabbitMqConfiguration {
    public static final String exchangeName="springboot-dead-exchange";
    /**
     * 声明死信交换机
     */
    @Bean
    public DirectExchange deadDirectExchange(){
        return new DirectExchange(exchangeName,true,false);
    }
    /**
     * 声明死信队列，和一般的队列没有什么区别
     */
    @Bean
    public Queue deadQueue(){
        return new Queue("springboot-dead-queue",true);
    }
    /**
     * 绑定交换机和死信队列
     */
    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue()).to(deadDirectExchange()).with("dead");
    }


}
