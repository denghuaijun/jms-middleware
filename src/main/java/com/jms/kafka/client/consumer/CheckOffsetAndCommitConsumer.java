package com.jms.kafka.client.consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * 同步提交
 * 测试手动同步提交位移
 * 手动提交有一个缺点，那就是当发起提交调用时应用会阻塞，我们可以减少手动提交的频率，但会增加重复消息的概率
 */
public class CheckOffsetAndCommitConsumer {
    private static final String broker_server="192.168.204.110:9092";
    private static final String kafka_topic="tp01";
    private static final String group_id="test_group";
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


        TopicPartition topicPartition = new TopicPartition(kafka_topic, 0);
        Set<TopicPartition> topicPartitionSet = new HashSet<>();
        topicPartitionSet.add(topicPartition);
        consumer.assign(topicPartitionSet);
        long lastConSumerOffset=-1L;
        int count =0;
        while(true){
            count++;
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            if (records.isEmpty()){
                System.out.println("第"+count+"次没有拉取到消息");
            }else {
                System.out.println("第"+count+"次拉取到消息77777");
                List<ConsumerRecord<String, String>> recordList = records.records(topicPartition);
                ConsumerRecord<String, String> lastConsumerRecord = recordList.get(recordList.size() - 1);
                lastConSumerOffset = lastConsumerRecord.offset();
                consumer.commitSync();//同步提交
                break;
            }
        }
        System.out.println("最后一次消费的位移："+lastConSumerOffset);
        Map<TopicPartition, OffsetAndMetadata> metadataMap = consumer.committed(topicPartitionSet);
        OffsetAndMetadata offsetAndMetadata = metadataMap.get(topicPartition);
        System.out.println("本次提交的位移为："+offsetAndMetadata.offset());
        System.out.println("下次要消费的位移为："+consumer.position(topicPartition));

    }
}
