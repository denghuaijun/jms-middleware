package com.jms.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq 模式之简单模式-生产者
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
        //4、通过通道创建交换机，声明队列，绑定关系，路由key，发送消息，接收消息
        //使用图形化界面绑定关系写死
        String exchangeName="topic_exchange";
         String routingKey="com.user.xxx";
         String exchgeType="topic";
        String msg="hello,topic exchange msg!!!";
        //5、准备消息内容
        /**
         * @param1 交换机
         * @param2 队列或者路由key
         * @param3 消息的状态控制
         * @param4 消息内容字节数组
         * 面试题：可以存在没有交换机的队列吗？不可以，虽然没有显式的指定交换机，但是会有一个默认的
         */
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        System.out.println("生产者发送消息到交换机"+exchangeName+"成功。。。");
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
