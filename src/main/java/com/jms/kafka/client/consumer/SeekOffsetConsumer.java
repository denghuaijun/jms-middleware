package com.jms.kafka.client.consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 指定位移消费，这样可以进行对消息的回溯，将会获取到该消费者从指定的位移的所有消费信息：
 * 结果：
 * 获取到消费者消费的分区信息：[tp01-0, tp01-1]
 * offset:10---消费的消息为：测试消费位移同步提交
 * offset:11---消费的消息为：测试消费位移同步提交
 * offset:12---消费的消息为：测试消费位移同步提交
 * offset:13---消费的消息为：测试消费位移同步提交
 * offset:14---消费的消息为：测试消费位移同步提交
 */
public class SeekOffsetConsumer {
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
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(kafka_topic));
        //timeout 参数设置太短会使分区获取失败，太长又会造成很多不必要的等待,如果把超时时间设置为1000就获取不到分区，
        consumer.poll(Duration.ofMillis(2000));
        //获取当前消费者被分配的分区
        Set<TopicPartition> assignment = consumer.assignment();
        /// 方法2 ：我们当然也可以添加一个死循环判断是否获取到分区信息的逻辑，获取到就跳出继续走，或者一直获取，
        // 这样就可以避免不知道将拉取的超时时间设置为多少合适，而造成不必要的等待  todo 暂时注释掉
        /*Set<TopicPartition> assignment = new HashSet<>();
        while(CollectionUtils.isEmpty(assignment)){
            consumer.poll(Duration.ofMillis(100));
            assignment=consumer.assignment();
        }*/
        System.out.println("获取到消费者消费的分区信息："+assignment);
        for (TopicPartition tp:assignment){
            //将所分配的所有分区全部指定从分区的10位移开始消费
            consumer.seek(tp,10);
        }
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> record:consumerRecords){
                System.out.println("offset:"+record.offset()+"---消费的消息为："+record.value());
            }
        }

    }
}
