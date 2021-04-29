package com.jms.kafka.client.producer;

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
public class KafkaProducerFirst {
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
        //properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG,)
        //添加自定义拦截器
        //properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProducerInteceptorPrefix.class.getName());
        KafkaProducer<String,String> kafkaProducer=new KafkaProducer<String, String>(properties);
        ProducerRecord<String,String> record=new ProducerRecord<>(kafka_topic,"kafka_test_topic_key","hello,java kafka123132132132");
        try{
            //这个为同步阻塞操作，要执行后续逻辑必须调用future的get方法等待kafka响应结束之后才能进行后续逻辑
//            Future<RecordMetadata> send = kafkaProducer.send(record);
//            RecordMetadata recordMetadata = send.get();
//            System.out.println("主题为："+kafka_topic+"--->偏移量："+recordMetadata.offset()+"--->所在分区："+recordMetadata.partition());
            //2、这个是异步操作
            kafkaProducer.send(record,(metadata,e)->{
                if (e !=null){
                    System.out.println("主题为："+kafka_topic+"发送消息失败，失败原因为："+e.getMessage());
                }else {
                    System.out.println("主题为："+kafka_topic+"--->偏移量："+metadata.offset()+"--->所在分区："+metadata.partition());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (kafkaProducer !=null){
                kafkaProducer.close();
                kafkaProducer.flush();
            }
        }
    }
}
