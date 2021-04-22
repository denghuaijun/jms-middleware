package com.jms.rabbitmq;

import com.jms.rabbitmq.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerTest {

    @Autowired
    private OrderService orderService;

    /**
     * 测试发布订阅模式
     */
    @Test
    public void testFanout(){
        orderService.sendFanoutMsg("1","1","1");
    }

    /**
     * 测试direct路由模式
     */
    @Test
    public void testDirect(){
        orderService.sendDirectMsg("1","1","1");
    }
    /**
     * 测试主题模式
     */
    @Test
    public void testTopic(){
        orderService.sendTopicMsg("1","1","1");

    }
    /**
     * 测试队列的TTL设置模式
     */
    @Test
    public void testTTLQueue(){
        for (int i = 0; i <11 ; i++) {
            orderService.sendTTLQueueMsg("1","1","1");
        }
    }
    /**
     * 测试message的TTL设置模式
     */
    @Test
    public void testTTLMsg(){
        orderService.sendTTLMsg("1","1","1");

    }
}
