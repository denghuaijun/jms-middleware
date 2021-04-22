package com.jms.rabbitmq.order.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * 查询业务消息本地冗余表，状态为0的数据，进行重新发送并更新重新发送的次数
 */
@EnableScheduling
public class MqTask {

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "")
    public void retrySendMsg(){
        //1、 查询MQ冗余表状态为未投递的订单数据
        //2、进行重新发送MQ
        //3、若发送成功在回执确认中更新状态，若失败更新重试次数
    }
}
