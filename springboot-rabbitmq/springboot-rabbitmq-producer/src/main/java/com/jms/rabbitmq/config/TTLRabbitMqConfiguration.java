package com.jms.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加rabbitmq队列，交换机及绑定关系声明配置类 使用direct类型来测试我们的队列过期TTL设置问题
 */
@Configuration
public class TTLRabbitMqConfiguration {
    public static final String exchangeName="springboot-ttl-exchange";
    /**
     * 声明交换机 类型Fanout
     */
    @Bean
    public DirectExchange ttlDirectExchange(){
        return new DirectExchange(exchangeName,true,false);
    }
    /**
     * 声明过期队列,并设置队列的过期时间
     * 死信产生的原因
     * 1、队列过期
     * 2、队列达到最大长度
     * 3、消息被拒绝
     * 此示例只测试死信产生原因的1、2的原因
     */
    @Bean
    public Queue ttlQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-message-ttl",5000);//队列过期时间/毫秒，若是超过5s，则也会进入DLX中
        map.put("x-max-length",5);//配置该队列的最大长度，若是超过则就会进入指定的DLX中
        //为过期队列设置死信交换机、死信路由
        map.put("x-dead-letter-exchange","springboot-dead-exchange");
        map.put("x-dead-letter-routing-key","dead");
        return new Queue("springboot-ttl-queue",true,false,false,map);
    }
    /**
     * 声明队列2，用于测试过期消息
     */
    @Bean
    public Queue ttlMsgQueue(){
        return new Queue("springboot-ttl-msg-queue",true,false,false);
    }
    /**
     * 声明绑定关系 广播模式没有路由key
     */
    @Bean
    public Binding ttlBinding(){
        return BindingBuilder.bind(ttlQueue()).to(ttlDirectExchange()).with("ttl");
    }
    /**
     * 声明绑定关系 广播模式没有路由key
     */
    @Bean
    public Binding ttlMsgBinding(){
        return BindingBuilder.bind(ttlMsgQueue()).to(ttlDirectExchange()).with("ttlMsg");
    }

}
