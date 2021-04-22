package com.jms.rabbitmq.order.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * MQ本地订单消息冗余表,实现生产可靠性
 */
@Data
public class MqOrderMessage implements Serializable {
    private static final long serialVersionUID=-1L;
    private Long id;
    private String orderId;
    private String orderContent;
    private Date createTime;
    private Integer msgStatus;
    private Integer retryCount;
}
