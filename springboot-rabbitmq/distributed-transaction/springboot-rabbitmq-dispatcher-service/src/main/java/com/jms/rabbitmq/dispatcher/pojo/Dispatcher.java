package com.jms.rabbitmq.dispatcher.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Dispatcher implements Serializable {
    private String orderId;
    private Integer userId;
    private String orderContent;
    private Date createTime;
}
