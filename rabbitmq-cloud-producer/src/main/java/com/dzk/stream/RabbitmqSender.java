package com.dzk.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dzk on 2018/11/17.
 */
@EnableBinding(Barista.class)
@Service
public class RabbitmqSender {

    @Autowired
    //@Qualifier("output_channel_new")
    Barista barista;

    // 发送消息
    public String sendMessage(Object message, Map<String, Object> properties) throws Exception {
        try{
            MessageHeaders mhs = new MessageHeaders(properties);
            Message msg = MessageBuilder.createMessage(message, mhs);
            boolean sendStatus = barista.logoutput().send(msg);
            System.err.println("--------------sending -------------------");
            System.out.println("发送数据：" + message + ",sendStatus: " + sendStatus);
        }catch (Exception e){
            System.err.println("-------------error-------------");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());

        }
        return null;
    }
}
