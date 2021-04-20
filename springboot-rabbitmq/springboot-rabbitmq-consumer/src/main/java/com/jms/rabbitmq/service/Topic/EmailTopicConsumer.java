package com.jms.rabbitmq.service.Topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

/**
 * 微信端消费者
 */
@Service
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "springboot-topic-email-q2",durable = "true",exclusive = "false",autoDelete = "false"),
        exchange = @Exchange(value = "springboot-topic-exchange",durable = "true",autoDelete = "false",type = ExchangeTypes.TOPIC),
        key="*.email.#"
))
public class EmailTopicConsumer {

    @RabbitHandler
    public void reviveMsg(String message){
        System.out.println("邮件 topic 接收到订单："+message);
    }
}
