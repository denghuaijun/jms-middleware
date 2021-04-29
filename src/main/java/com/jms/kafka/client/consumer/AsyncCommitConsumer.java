package com.jms.kafka.client.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *异步提交
 * 缺点：
 * 如果服务器返回提交失败，异步提交不会进行重试，相比同步，同步会进行重试直到成功或者抛出异常给应用，
 * 异步提交没有重试是因为，多个消费在同时进行异步提交时会导致位移覆盖出现重复消费。
 */
public class AsyncCommitConsumer {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="tp01";
    private static final String group_id="test_group";
    private static AtomicBoolean running = new AtomicBoolean(true);
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,group_id);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        //关闭自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(kafka_topic));
        try{
            while(running.get()){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                consumerRecords.forEach(consumerRecord->{
                    System.out.println(consumerRecord.value());
                    // do something
                });
                //异步回调
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception ==null){
                            System.out.println("Map<TopicPartition, OffsetAndMetadata>=======:"+offsets);
                        }else {
                            System.out.println("提交offset失败，失败原因exception:"+exception);
                        }
                    }
                });
            }
        }finally {
            consumer.close();
        }


    }
}
