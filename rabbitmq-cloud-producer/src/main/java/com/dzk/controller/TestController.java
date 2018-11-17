package com.dzk.controller;

import com.dzk.stream.RabbitmqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dzk on 2018/11/17.
 */
@RestController
public class TestController {

    @Autowired
    RabbitmqSender sender;

    @GetMapping("send")
    public String seed() throws Exception{
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("SERIAL_NUMBER", "12345");
        properties.put("BANK_NUMBER", "abc");
        //properties.put("PLAT_SEND_TIME", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        sender.sendMessage("Hello, I am amqp sender num :" , properties);
        return "seed";
    }
}
