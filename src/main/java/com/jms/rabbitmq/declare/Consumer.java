package com.jms.rabbitmq.declare;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitmq 模式之简单模式-消费者
 */
public class Consumer {

    private static Runnable runnable=new Runnable() {
        @Override
        public void run() {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setVirtualHost("/");
            factory.setUsername("admin");
            factory.setPassword("admin");
            factory.setHost("192.168.204.110");
            factory.setPort(5672);
            Connection connection = null;
            Channel channel=null;
            try {
                connection = factory.newConnection();
                channel = connection.createChannel();
                //消费队列
                String queueName = Thread.currentThread().getName();
                channel.basicConsume(queueName, true,(msgTag,msg)->{
                    System.out.println(queueName+"======msgTag:"+msgTag);
                    System.out.println(queueName+"=====接收到消息msg:"+new String(msg.getBody(),"UTF-8"));
                },(msgTag)->{
                    System.out.println(queueName+"接收失败");
                });
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }finally {
                    try {
                        if (channel !=null && channel.isOpen()) {
                            channel.close();
                        }
                        if (connection !=null && connection.isOpen()){
                            connection.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

            }
        }
    };

    public static void main(String[] args) throws IOException, TimeoutException {
        //消费者监听队列
       new Thread(runnable,"queue1").start();
        new Thread(runnable,"queue2").start();
        new Thread(runnable,"queue3").start();
        new Thread(runnable,"queue4").start();
        new Thread(runnable,"java_queue1").start();
        new Thread(runnable,"java_queue2").start();
        new Thread(runnable,"java_queue3").start();

    }
}
