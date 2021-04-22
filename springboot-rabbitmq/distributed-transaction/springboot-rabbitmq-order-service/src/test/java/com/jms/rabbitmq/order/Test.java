package com.jms.rabbitmq.order;

import com.jms.rabbitmq.order.pojo.Order;
import com.jms.rabbitmq.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {

    @Autowired
    private OrderService orderService;

    @org.junit.jupiter.api.Test
    public void createOrder() throws Exception {
        Order order =new Order();
        order.setOrderId("11111");
        order.setOrderContent("黄焖鸡米饭");
        order.setUserId(1);
        orderService.createOrder(order);
    }

    /**
     * 模拟调用配送系统超时。。模拟分布式事务问题
     * 订单会回滚不存表，运单服务仍然开始配送，模拟数据不一致问题
     * @throws Exception
     */
    @org.junit.jupiter.api.Test
    public void createOrder2() throws Exception {
        Order order =new Order();
        order.setOrderId("12345");
        order.setOrderContent("分布式事务测试1");
        order.setUserId(2);
        orderService.createOrder(order);
    }
}
