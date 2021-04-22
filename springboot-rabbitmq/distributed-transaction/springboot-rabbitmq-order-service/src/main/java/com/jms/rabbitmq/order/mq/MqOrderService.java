package com.jms.rabbitmq.order.mq;

import com.alibaba.fastjson.JSON;
import com.jms.rabbitmq.order.dao.OrderDao;
import com.jms.rabbitmq.order.pojo.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 使用MQ来进行发送订单信息
 */
@Service
public class MqOrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderDao orderDao;
    /*@PostConstruct 注解是java自己的注解
    @PostConstruct 是来修饰一个非静态的void方法，被这个注解修饰的方法会在服务器加载servlet时执行，也就是在bean注入到IOC容器后执行
    并且只会被执行一次，PostConstruct在构造方法之后init方法之前执行
    */
    @PostConstruct
    public void confirmMsg(){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("cause===:"+cause);
            //如果ack为true代表消息已经收到
            String orderId = correlationData.getId();
            System.out.println("回调确认订单ID："+orderId);
            if (!ack){
                //消息未收到处理，可以修改表的状态记录失败的原因
                System.out.println("MQ队列应答失败，订单ID=："+orderId);
                return;
            }
            //如果交换机收到消息，那么就更新本地消息冗余表msg_status为1
            try{
                int i = orderDao.updateMsgStatusByOrderId(orderId);
                if (i ==1){
                    System.out.println("MQ订单消息冗余表更新状态成功，订单信息已正常投入到MQ队列中");
                }
            }catch (Exception e){

                    System.out.println("MQ订单消息冗余表更新状态失败，出现异常："+e.getMessage());

            }

        });
    }
    public void sendOrder(Order order){
        String exchangeName="order-fanout-exchange";
        String routingKey="";
        rabbitTemplate.convertAndSend(exchangeName,routingKey, JSON.toJSONString(order),new CorrelationData(order.getOrderId()));
    }
}
