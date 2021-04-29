package com.jms.kafka.client.producer;

import com.jms.kafka.config.MyDefinePartitioner;
import com.jms.kafka.config.MyProducerInteceptorPrefix;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 第一个kafka生产者
 * 消息类型为字符串类型
 */
public class KafkaInteceptorProducerFirst {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="test_topic";
    public static void main(String[] args) {
        Properties properties = new Properties();
        //kafka主题的key序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //kafka主题的值序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,3);
        //kafka的服务地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);
        //设置分区
        properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyDefinePartitioner.class.getName());
        //添加自定义拦截器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProducerInteceptorPrefix.class.getName());
        KafkaProducer<String,String> kafkaProducer=new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record=new ProducerRecord<>(kafka_topic,"kafka_interceptor_key","hello,java kafka interceptor");
        try{
            kafkaProducer.send(record);
        }catch (Exception e){
            e.printStackTrace();
        }
        kafkaProducer.close();
    }
}
