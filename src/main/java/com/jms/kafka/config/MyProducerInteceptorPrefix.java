package com.jms.kafka.config;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * Producer拦截器是个新的功能，它和消费端的拦截器在kafka0.10版本被注入的，主要用于实现clinets段的定制化控制逻辑
 * 使用场景：
 * 1、按照某个规则过滤掉不符合要求的消息
 * 2、修改消息的内容
 * 3、统计类需求
 *
 * 以下为自定义消息前缀拦截器
 */
public class MyProducerInteceptorPrefix implements ProducerInterceptor<String,String> {
    private volatile long sendSuccess=0;
    private volatile long sendFailure=0;


    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        //修改消息值
        String modifiedValue="prefix_"+record.value();

        return new ProducerRecord<String, String>(record.topic(),record.partition(),record.timestamp(),record.key(),modifiedValue,record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception ==null){
            sendSuccess++;
        }else {
            sendFailure++;
        }
    }

    @Override
    public void close() {
        double successRatio=(double) sendSuccess/(sendFailure+sendSuccess);
        System.out.println("########## 消息发送成功率="+String.format("%f",(successRatio*100)+"%"));
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}

