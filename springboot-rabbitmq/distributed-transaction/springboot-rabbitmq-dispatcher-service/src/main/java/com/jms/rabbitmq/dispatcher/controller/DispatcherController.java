package com.jms.rabbitmq.dispatcher.controller;

import com.jms.rabbitmq.dispatcher.dao.DispatcherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {

    @Autowired
    private DispatcherDao dispatcherDao;

    @GetMapping("/order")
    public String createDispatcher(String orderId)throws Exception{
        if ("12345".equals(orderId)){
            Thread.sleep(5000);//业务模拟接口调用超时，这样订单系统又事务控制就会事务回滚，但是配送系统一样会进行配送，这样模拟俩个服务中事务一致性问题
        }
        dispatcherDao.insertDispatcher(orderId);
        return "success";
    }
}
