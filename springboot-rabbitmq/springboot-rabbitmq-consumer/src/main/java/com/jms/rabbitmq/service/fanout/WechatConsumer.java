package com.jms.rabbitmq.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 微信端消费者
 */
@Service
@RabbitListener(queues = {"springboot-fanout-wechat-q3"})
public class WechatConsumer {

    @RabbitHandler
    public void reviveMsg(String message){
        System.out.println("wechat fanout 接收到订单："+message);
    }
}
