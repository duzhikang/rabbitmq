package com.dzk.service;

import com.dzk.bean.Order;
import org.springframework.stereotype.Service;

/**
 * Created by dzk on 2018/11/17.
 */
@Service
public class OrderService {

    public void handle(Order order) {
        order.toString();
    }
}
