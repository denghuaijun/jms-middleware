package com.jms.rabbitmq.work.fair_distribution;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq 模模式之工作模式-公平分发生产者
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //所有的中间件技术都是基于TCP/IP协议基础之上构建的新型协议规范，而RabbitMQ使用的是amqp协议
        //ip:port
        //1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.204.110");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");//把消息发送到我们本机的根节点
        //2、创建连接connection
        Connection connection = factory.newConnection("生产者");
        //3、通过连接获取通道channel
        Channel channel = connection.createChannel();
        //声明队列
        String queueName="work_queue1";
        channel.queueDeclare(queueName,true,false,false,null);
        //5、准备消息内容
        /**
         * @param1 交换机
         * @param2 队列或者路由key
         * @param3 消息的状态控制
         * @param4 消息内容字节数组
         * 面试题：可以存在没有交换机的队列吗？不可以，虽然没有显式的指定交换机，但是会有一个默认的
         */
        for (int i=0;i<20;i++){
            String msg="测试woker模式的轮询策略"+i;
            channel.basicPublish("",queueName,null,msg.getBytes());
        }
        System.out.println("消息发送成功。。。");
        //6、接收消息内容
        //7、关闭通道
        if (channel !=null && channel.isOpen()){
            channel.close();
        }
        //8、关闭连接
        if (connection !=null && connection.isOpen()){
            connection.close();
        }
    }
}
