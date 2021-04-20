package com.jms.rabbitmq.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 微信端消费者
 */
@Service
@RabbitListener(queues = {"springboot-direct-email-q2"})
public class EmailDirectConsumer {

    @RabbitHandler
    public void reviveMsg(String message){
        System.out.println("邮件 fanout 接收到订单："+message);
    }
}
