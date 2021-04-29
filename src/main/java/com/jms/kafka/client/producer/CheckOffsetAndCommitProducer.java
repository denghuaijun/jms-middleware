package com.jms.kafka.client.producer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 测试同步提交位移
 */
public class CheckOffsetAndCommitProducer {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="tp01";
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);
        properties.put(ProducerConfig.RETRIES_CONFIG,10);

        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> producerRecord = new ProducerRecord<String, String>(kafka_topic,0,"kafka-demo01","测试消费位移同步提交0429");
        try{
            Future<RecordMetadata> send = producer.send(producerRecord);
            RecordMetadata recordMetadata = send.get();
            long offset = recordMetadata.offset();
            System.out.println("消息发送的位移offset:"+offset);
            System.out.println("消息发送主题:"+recordMetadata.topic()+"对应的分区位置为："+recordMetadata.partition());
            System.out.println("========================================================");
        }catch (Exception e){
            e.printStackTrace();
        }
        producer.close();
    }
}
