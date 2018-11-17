package com.dzk.listener;

import com.dzk.bean.Order;
import com.dzk.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * Created by dzk on 2018/11/17.
 */
@EnableBinding(Sink.class)
public class OrderHandler {

    @Autowired
    OrderService orderService;

    @StreamListener(Sink.INPUT)
    public void handle(Order order) {

        orderService.handle(order);
    }
}
