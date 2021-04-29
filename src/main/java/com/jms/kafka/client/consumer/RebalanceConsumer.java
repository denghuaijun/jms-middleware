package com.jms.kafka.client.consumer;

import com.jms.kafka.client.common.PropertiesConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 在均衡消费：
 * 在均衡是之分区的所属从一个消费者转移到另一个消费者的行为，它为消费者组具备高可用和伸缩性提供了保证，使得
 * 我们即安全又方便的对消费组内的消费者进行增删，不过在发生在均衡期间，消费者是无法拉取消息的
 */
public class RebalanceConsumer {
    private static final String topic="tp01";
    private static final String group_id="test_group";

    private static AtomicBoolean running = new AtomicBoolean(true);
    public static void main(String[] args) {
        Properties properties = PropertiesConfig.initConsumerConfig(group_id, true);
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        Map<TopicPartition,OffsetAndMetadata> currentMap = new HashMap<>();
        //消费者在均衡的时候使用subscribe这个方法，在消费者发生在均衡时会调用此监听器
        consumer.subscribe(Collections.singleton(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                consumer.commitSync(currentMap);
            }
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                //在均衡添加一些其它逻辑业务
            }
        });
        //拉取信息
        try{
            while(running.get()){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    System.out.println("当前消费消息的位移："+record.offset()+"--->消息为："+record.value()+"----所在分区："+record.partition());
                    //使用map记录当前消费的分区及所在的位移，这样在消费者发生在均衡的时候，就会调用上面的监听器，提交当前的位移信息，从而避免消息丢失
                    currentMap.put(new TopicPartition(topic,record.partition()),new OffsetAndMetadata(record.offset()+1));
                }
                consumer.commitAsync(currentMap,null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            consumer.close();
        }

    }
}
