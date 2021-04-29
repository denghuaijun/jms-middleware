package com.jms.kafka.config;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * KafkaAdminClient功能系统代码实现
 */
public class KafkaAdminClientOperation {
    private static final String broker_server_list="192.168.204.110:9092";
    private static final String topic="tp01";
    private static AdminClient adminClient;
    static {
        Properties properties =new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,broker_server_list);
        properties.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG,"3000");
        adminClient = AdminClient.create(properties);
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
         //describeTopicConfig();
         //alterTopicConfig();
         //addTopicPartitions();
    }

    /**
     * 修改主题配置信息
     */
    private static void alterTopicConfig() throws ExecutionException, InterruptedException {
        ConfigResource resource=new ConfigResource(ConfigResource.Type.TOPIC,topic);
        ConfigEntry configEntry = new ConfigEntry("cleanup.policy","compact");
        Config config =new Config(Collections.singleton(configEntry));
        Map<ConfigResource,Config> map = new HashMap<>();
        map.put(resource,config);
        AlterConfigsResult result = adminClient.alterConfigs(map);
        result.all().get();
        System.out.println("修改主题配置成功。。。");
    }

    /**
     * 新增主题分区数
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void addTopicPartitions() throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> map = new HashMap<>();
        NewPartitions newPartitions = NewPartitions.increaseTo(4);
        map.put(topic,newPartitions);
        CreatePartitionsResult partitions = adminClient.createPartitions(map);
        partitions.all().get();
        System.out.println("新增主题分区成功。。。。");
    }

    /**
     * 获取主题详情
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void describeTopicConfig() throws ExecutionException, InterruptedException {

        ConfigResource resource=new ConfigResource(ConfigResource.Type.TOPIC,topic);
        DescribeConfigsResult result = adminClient.describeConfigs(Arrays.asList(resource));
        Config config = result.all().get().get(resource);
        System.out.println("describeTopicConfig=====>"+config);
        //adminClient.close();
    }

}
