package com.jms.rabbitmq.order.dao;

import com.jms.rabbitmq.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存订单信息
     * @param order
     * @return
     */
    public int saveOrder(Order order) throws Exception {
        String sql = "insert into `order` (order_id,user_id,order_content) values(?,?,?)";
        int update = jdbcTemplate.update(sql, order.getOrderId(), order.getUserId(), order.getOrderContent());
        if (update !=1){
            throw new Exception("保存订单失败。。。。");
        }
        //如果保存成功，为了保证生产可靠性，同时保存本地消息订单冗余表
        int i=saveMqOrder(order);
        if (i !=1){
            throw new Exception("保存订单失败，原因【保存MQ消息冗余表失败】");
        }
        return update;
    }

    public int saveMqOrder(Order order) {
        String sql="insert into mq_order_message(order_id,msg_status,order_content,retry_count) values (?,?,?,?)";
        int i=jdbcTemplate.update(sql,order.getOrderId(),0,order.getOrderContent(),0);
        return i;
    }

    /**
     * 根据订单ID根据本地订单消息表冗余消息投入队列状态
     * @param id
     * @return
     */
    public int updateMsgStatusByOrderId(String id) {
        String sql="update mq_order_message set msg_status=1 where order_id=?";
        int i=jdbcTemplate.update(sql,id);
        return i;
    }
}
