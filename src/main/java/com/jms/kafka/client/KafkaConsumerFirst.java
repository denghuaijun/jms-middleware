package com.jms.kafka.client;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 第一个kafka消费者，
 * 消息类型为字符串
 * 默认的还有int 字节数组类型
 */
public class KafkaConsumerFirst {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="test_topic";
    private static final String group_id="test_group";
    public static void main(String[] args) {
        Properties properties = new Properties();
        //kafka主题的key序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //kafka主题的值序列化
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //kafka的服务地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,group_id);
        //设置分区
        //properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG,)
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(kafka_topic));
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(10000));
            for (ConsumerRecord<String,String> record:consumerRecords) {
                System.out.println("收到主题："+kafka_topic+"的消息为："+record.value());
            }
        }
    }
}
