package com.jms.kafka.client.consumer;

import com.jms.kafka.client.common.PropertiesConfig;
import com.jms.kafka.config.MyConsumerInterceptorTTL;
import kafka.server.KafkaConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 消费者过期消息拦截器配置测试类
 */
public class ConsumerTTL {
    private static final String topic="tp01";
    private static final String group_id="test_group";

    private static AtomicBoolean running = new AtomicBoolean(true);
    public static void main(String[] args) {
        Properties properties = PropertiesConfig.initConsumerConfig(group_id, true);
        //过期消息拦截器
        properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MyConsumerInterceptorTTL.class.getName());
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singleton(topic));
        //拉取信息
        try{
            while(running.get()){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    System.out.println("当前消费消息的位移："+record.offset()+"--->消息为："+record.value()+"----所在分区："+record.partition());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }

    }
}
