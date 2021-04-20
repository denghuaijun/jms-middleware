package com.jms.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 模拟下订单成功之后发送短信、邮件、微信通知
 */
@Service
public class OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用广播的方式发送订单
     * @param userId
     * @param productId
     * @param orderNum
     */
    public void sendFanoutMsg(String userId,String productId,String orderNum){
        //1、根据productId查询库存数量是否足够
        //2、下单成功保存订单，并生成订单ID
        String orderId = UUID.randomUUID().toString();
        //3、发送订单信息
        String exchangeName="springboot-fanout-exchange";
        rabbitTemplate.convertAndSend(exchangeName,"",orderId);
        System.out.println("发送订单信息成功。。。");
    }

    /**
     * 使用路由direct的方式发送订单
     * @param userId
     * @param productId
     * @param orderNum
     */
    public void sendDirectMsg(String userId,String productId,String orderNum){
        //1、根据productId查询库存数量是否足够
        //2、下单成功保存订单，并生成订单ID
        String orderId = UUID.randomUUID().toString();
        //3、发送订单信息
        String exchangeName="springboot-direct-exchange";
        String routingKey="order";
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
        System.out.println("发送订单信息成功。。。");
    }

    /**
     * 使用路由Topic的方式发送订单
     * @param userId
     * @param productId
     * @param orderNum
     */
    public void sendTopicMsg(String userId,String productId,String orderNum){
        //1、根据productId查询库存数量是否足够
        //2、下单成功保存订单，并生成订单ID
        String orderId = UUID.randomUUID().toString();
        //3、发送订单信息
        String exchangeName="springboot-topic-exchange";
        String routingKey="com.order.xx.xx";
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
        System.out.println("发送订单信息成功。。。");
    }
}
