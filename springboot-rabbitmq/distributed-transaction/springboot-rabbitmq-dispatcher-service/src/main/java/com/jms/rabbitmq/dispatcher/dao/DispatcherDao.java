package com.jms.rabbitmq.dispatcher.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DispatcherDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存派单信息
     * @param orderId
     * @return
     */
    public int insertDispatcher(String orderId)throws Exception{
        String sql="insert into dispatcher(id,order_id,user_id,order_content,status) values(?,?,?,?,?)";
        int i = jdbcTemplate.update(sql, null, orderId, UUID.randomUUID().toString(), "订单信息", "1");
        if (i !=1){
            throw new Exception("订单id为"+orderId+"的派单信息数据库保存失败");
        }
        return i;
    }
}
