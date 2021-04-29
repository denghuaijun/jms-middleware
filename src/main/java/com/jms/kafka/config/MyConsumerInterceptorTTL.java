package com.jms.kafka.config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义消费者拦截器，对消费消息设置一个有效期，如果某条消息在一定的时间内没有到达，就不再对其进行处理
 */
public class MyConsumerInterceptorTTL implements ConsumerInterceptor<String,String> {
    private static final  long expire_interval=10*1000;//设置消息过期时间为10s，若10s内还未处理就视为过期
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        long now = System.currentTimeMillis();
        Map<TopicPartition, List<ConsumerRecord<String,String>>> recordsMap = new ConcurrentHashMap<>();
        Set<TopicPartition> partitions = records.partitions();
        partitions.forEach(topicPartition -> {
            List<ConsumerRecord<String, String>> consumerRecordList = records.records(topicPartition);
            List<ConsumerRecord<String, String>> newRecordList=new ArrayList<>();
            consumerRecordList.forEach(record -> {
                long timestamp = record.timestamp();
                if (now-timestamp<expire_interval){
                    newRecordList.add(record);
                }
            });
            if (CollectionUtils.isNotEmpty(newRecordList)){
                recordsMap.put(topicPartition,newRecordList);
            }
        });
        return new ConsumerRecords<String, String>(recordsMap);
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.forEach((tp,offsetMeta)-> System.out.println("MyConsumerInterceptorTTL当前提交的位移："+offsetMeta.offset()+"--分区信息："+tp));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
