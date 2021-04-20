package com.jms.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 添加rabbitmq队列，交换机及绑定关系声明配置类
 */
@Configuration
public class DirectRabbitMqConfiguration {
    public static final String exchangeName="springboot-direct-exchange";
    /**
     * 声明交换机 类型direct
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName,true,false);
    }
    /**
     * 声明队列1
     */
    @Bean
    public Queue queueD1(){
        return new Queue("springboot-direct-dx-q1",true,false,false);
    }
    /**
     * 声明队列2
     */
    @Bean
    public Queue queueD2(){
        return new Queue("springboot-direct-email-q2",true,false,false);
    }
    /**
     * 声明队列3
     */
    @Bean
    public Queue queueD3(){
        return new Queue("springboot-direct-wechat-q3",true,false,false);
    }
    /**
     * 声明绑定关系 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueueD1(){
        return BindingBuilder.bind(queueD1()).to(directExchange()).with("order");
    }
    /**
     * 声明绑定关系2 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueueD2(){
        return BindingBuilder.bind(queueD2()).to(directExchange()).with("user");

    }
    /**
     * 声明绑定关系3 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueueD3(){
        return BindingBuilder.bind(queueD3()).to(directExchange()).with("order");

    }
}
