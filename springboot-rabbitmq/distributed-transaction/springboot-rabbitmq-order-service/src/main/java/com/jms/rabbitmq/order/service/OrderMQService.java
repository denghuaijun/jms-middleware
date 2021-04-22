package com.jms.rabbitmq.order.service;

import com.jms.rabbitmq.order.dao.OrderDao;
import com.jms.rabbitmq.order.mq.MqOrderService;
import com.jms.rabbitmq.order.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 使用rabbitMQ来解决分布式事务
 */
@Slf4j
@Service
public class OrderMQService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    MqOrderService mqOrderService;

    @Transactional(rollbackFor = Exception.class)
    public void  createOrder(Order order)throws Exception{
        //1、下单成功，保存订单信息
        int i = orderDao.saveOrder(order);
        //2、通过MQ来进行发送派单
        mqOrderService.sendOrder(order);

    }

}
