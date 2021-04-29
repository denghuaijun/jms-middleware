package com.jms.kafka.client.common;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * kafka消费、生产者基础配置
 */
public class PropertiesConfig {
    private static final String broker_server="192.168.204.110:9092";
    /**
     * kafka生产配置信息
     * @return
     */
    public static Properties initProducerConfig(String topic){
        Properties properties = new Properties();
        //kafka主题的key序列化
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //kafka主题的值序列化
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,3);
        //kafka的服务地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);

        return properties;
    }
    /**
     * kafka消费者配置信息
     * @return
     */
    public static Properties initConsumerConfig(String consumerGroup,boolean isAutoCommit){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,consumerGroup);
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,isAutoCommit);
        return properties;
    }
}
