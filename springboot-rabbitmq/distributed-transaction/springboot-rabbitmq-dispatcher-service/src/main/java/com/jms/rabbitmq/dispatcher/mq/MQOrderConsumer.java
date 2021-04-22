package com.jms.rabbitmq.dispatcher.mq;

import com.alibaba.fastjson.JSON;
import com.jms.rabbitmq.dispatcher.dao.DispatcherDao;
import com.jms.rabbitmq.dispatcher.pojo.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class MQOrderConsumer {

    @Autowired
    private DispatcherDao dispatcherDao;

    private int count =1;

    /**
     * 消费者报错会出现什么情况？若都是默认配置的话
     * 消费者出现异常或者报错默认情况rabbitmq服务器会不断重试去发送消息，这样就会导致服务器内存占满，造成服务器宕机
     * 如何解决这样的重试问题？
     * 1、设置rabbitMQ的监听重试次数  这种方式达到一定次数之后会自动移除消息不可靠
     * 2、try。。catch+手动ack  此时设置重试次数已经没有意义
     * 3、try。。catch+手动ack+死信队列+人工干预
     *
     * @param orderJson
     * @param correlationData
     * @param channel
     * @param tag
     * @throws Exception
     */
    @RabbitListener(queues = "order-queue")
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
            dispatcherDao.insertDispatcher(order.getOrderId());
            System.out.println(1/0);//手动抛出异常
            //4、手动ACK告诉mq消息已经消费这样消息队列就没有了
            channel.basicAck(tag,false);
        }catch (Exception e){
            //如果出现异常的情况，根据实际情况去进行重发
            //重发一次后，丢失，还是记录，存库根据自己的业务去定
            //参数1 ：消息的tag 参数2：是否多条处理，参数三是否重发
            //false 是不重发，会把消息打入绑定的死信队列
            //true 会死循环的重发，如果使用true的话，不要加try。。catch，否则容易死循环
            //为什么在try。。catch中选择重发就会死循环呢，因为消息会被重复丢进队列中，然后同样走进catch中一直重复这个操作，所以还是推荐死信，同时加一个死信消费者类处理
            channel.basicNack(tag,false,false);//单条消息不重发扔掉/丢到死信

        }


    }

}
