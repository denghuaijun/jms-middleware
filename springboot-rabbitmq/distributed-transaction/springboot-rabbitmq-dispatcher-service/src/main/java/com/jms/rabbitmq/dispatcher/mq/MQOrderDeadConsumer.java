package com.jms.rabbitmq.dispatcher.mq;

import com.alibaba.fastjson.JSON;
import com.jms.rabbitmq.dispatcher.dao.DispatcherDao;
import com.jms.rabbitmq.dispatcher.pojo.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class MQOrderDeadConsumer {

    @Autowired
    private DispatcherDao dispatcherDao;

    private int count =1;

    /**
     *监听死信队列
     * @param orderJson
     * @param correlationData
     * @param channel
     * @param tag
     * @throws Exception
     */
    @RabbitListener(queues = "order-dead-queue")
    public void receive(String orderJson, CorrelationData correlationData,
                        Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG)long tag) throws Exception {
        try{
            //获取队列的消息
            String id = correlationData.getId();
            System.out.println("收到MQ的订单ID为："+id+"的消息是："+orderJson+",count="+ count++);
            //2、获取队列中的订单信息
            Order order = JSON.parseObject(orderJson, Order.class);
            //3、保存派单信息
            //要考虑幂等性问题，唯一主键/redis分布式锁来解决
            dispatcherDao.insertDispatcher(order.getOrderId());
            //4、手动ACK告诉mq消息已经消费这样消息队列就没有了
            channel.basicAck(tag,false);
        }catch (Exception e){
            //如果在死信消费者里面仍然出现错误那么我们可以采用以下处理
            //人工干预
            //发送短信
            //存储日志
            channel.basicNack(tag,false,false);//同时将消息去掉

        }


    }

}
