package com.jms.rabbitmq.simple;

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
        /**
         * @param1 队列的名称
         * @param2 是否持久化 durable true为持久化，false为非持久化，非持久化也会存盘但是会随着服务器重启而丢失
         * @param3 排他性，是否独占队列
         * @param4 是否自动删除 ，随着最后一个消费者消费完成后是否将队列删除
         * @param5 携带一些附加参数
         */
        String queueName ="test_queue";
        //声明消息队列
        channel.queueDeclare(queueName,false,false,false,null);
        String msg="hello,simple";
        //5、准备消息内容
        /**
         * @param1 交换机
         * @param2 队列或者路由key
         * @param3 消息的状态控制
         * @param4 消息内容字节数组
         * 面试题：可以存在没有交换机的队列吗？不可以，虽然没有显式的指定交换机，但是会有一个默认的
         */
        channel.basicPublish("",queueName,null,msg.getBytes());
        System.out.println("生产者发送消息到队列"+queueName+"成功。。。");
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
