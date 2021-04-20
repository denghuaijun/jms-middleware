package com.jms.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 添加rabbitmq队列，交换机及绑定关系声明配置类
 */
@Configuration
public class FanoutRabbitMqConfiguration {
    public static final String exchangeName="springboot-fanout-exchange";
    /**
     * 声明交换机 类型Fanout
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(exchangeName,true,false);
    }
    /**
     * 声明队列1
     */
    @Bean
    public Queue queueF1(){
        return new Queue("springboot-fanout-dx-q1",true,false,false);
    }
    /**
     * 声明队列2
     */
    @Bean
    public Queue queueF2(){
        return new Queue("springboot-fanout-email-q2",true,false,false);
    }
    /**
     * 声明队列3
     */
    @Bean
    public Queue queueF3(){
        return new Queue("springboot-fanout-wechat-q3",true,false,false);
    }
    /**
     * 声明绑定关系 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueue1(){
        return BindingBuilder.bind(queueF1()).to(fanoutExchange());
    }
    /**
     * 声明绑定关系2 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueue2(){
        return BindingBuilder.bind(queueF2()).to(fanoutExchange());
    }
    /**
     * 声明绑定关系3 广播模式没有路由key
     */
    @Bean
    public Binding bindingQueue3(){
        return BindingBuilder.bind(queueF3()).to(fanoutExchange());
    }
}
