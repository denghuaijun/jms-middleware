package com.jms.rabbitmq.order;

import com.jms.rabbitmq.order.pojo.Order;
import com.jms.rabbitmq.order.service.OrderMQService;
import com.jms.rabbitmq.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MQOrderTest {

    @Autowired
    private OrderMQService orderService;

    @org.junit.jupiter.api.Test
    public void createOrder() throws Exception {
        Order order =new Order();
        order.setOrderId("sdadsasd");
        order.setOrderContent("消费可靠性。。。");
        order.setUserId(1);
        orderService.createOrder(order);
    }

}
