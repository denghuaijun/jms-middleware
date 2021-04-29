package com.jms.kafka.client.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 默认自动提交
 * 第一个kafka消费者，
 * 消息类型为字符串
 * 默认的还有int 字节数组类型
 */
public class KafkaConsumerFirst {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="tp01";
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
        //通过subscribe（）方法接收一个主题列表来进行订阅消费主题信息
        //consumer.subscribe(Collections.singletonList(kafka_topic));
        //我们也可以通过正则表达式类匹配多个主题,如下将会匹配所有以test开头的主题
       // consumer.subscribe(Pattern.compile("test*"));
        //我们也可以指定消费对应的主题的某一个分区
        consumer.assign(Arrays.asList(new TopicPartition(kafka_topic,1)));
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(10));
            for (ConsumerRecord<String,String> record:consumerRecords) {
                long offset = record.offset();
                System.out.println("收到主题："+kafka_topic+"的消息为："+record.value()+"当前的位移为："+offset);
            }
        }
    }
}
