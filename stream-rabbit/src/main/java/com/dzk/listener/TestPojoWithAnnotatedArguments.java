package com.dzk.listener;

import com.dzk.bean.ComputeOrder;
import com.dzk.bean.FoodOrder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * Created by dzk on 2018/11/17.
 */
@EnableBinding(Sink.class)
public class TestPojoWithAnnotatedArguments {

    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='food'")
    public void receiveFoodOrder(@Payload FoodOrder foodOrder) {
        // handle the message
        System.out.println(foodOrder.toString());
    }
    @StreamListener(target = Sink.INPUT, condition = "headers['type']=='compute'")
    public void receiveComputeOrder(@Payload ComputeOrder computeOrder) {
        // handle the message
        System.out.println(computeOrder.toString());
    }
}
