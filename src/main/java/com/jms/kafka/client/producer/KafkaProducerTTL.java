package com.jms.kafka.client.producer;

import com.jms.kafka.client.common.PropertiesConfig;
import com.sun.jdi.PathSearchingVirtualMachine;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 配合测试消费者拦截器
 */
public class KafkaProducerTTL {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        Properties properties = PropertiesConfig.initProducerConfig("tp01");
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record = new ProducerRecord<String, String>("tp01",1, l -10*1000,"kafka-ttl","过期消息。。。。");
        ProducerRecord<String,String> record2 = new ProducerRecord<String, String>("tp01",1, System.currentTimeMillis(),"kafka-ttl","非过期消息。。。。");
        producer.send(record);
        producer.send(record2);
        //生产者对象必须close不然消息不会发送
        producer.close();
    }
}
