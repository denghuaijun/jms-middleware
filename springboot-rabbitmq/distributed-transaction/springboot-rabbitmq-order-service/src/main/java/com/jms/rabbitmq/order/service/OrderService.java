package com.jms.rabbitmq.order.service;

import com.jms.rabbitmq.order.dao.OrderDao;
import com.jms.rabbitmq.order.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 使用传统的restTemplate来模拟分布式事务场景
 */
@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    RestTemplate restTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void  createOrder(Order order)throws Exception{
        //1、下单成功，保存订单信息
        int i = orderDao.saveOrder(order);
        //2、通过http restTemplate远程调用派单服务
        String result=callDisapatcher(order.getOrderId());
        if (!"success".equals(result)){
            throw new Exception("订单创建失败，调用运单接口失败。。");
        }
    }

    private String callDisapatcher(String orderId) {
        String url="http://localhost:20202/dispatcher/order?orderId="+orderId;
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }
}
