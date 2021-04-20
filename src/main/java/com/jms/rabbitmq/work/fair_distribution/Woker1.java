package com.jms.rabbitmq.work.fair_distribution;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq 模式之工作模式-公平分发消费者
 */
public class Woker1 {
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
        Connection connection = null;
        connection = factory.newConnection("worker1-消费者");
        //3、通过连接获取通道channel
        Channel channel = connection.createChannel();
        String queueName ="work_queue1";

        //6、接收消息内容
        channel.basicConsume(queueName, false,(consumerTag,deliveryMsg)->{
           // System.out.println("msgTag:"+msgTag);
            System.out.println("msg:"+new String(deliveryMsg.getBody(),"UTF-8"));
            channel.basicAck(deliveryMsg.getEnvelope().getDeliveryTag(),false);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },(msgTag)->{
            System.out.println("接收失败");
        });
        //阻塞程序
        System.in.read();
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
