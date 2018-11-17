package com.hiram.producer;

import com.hiram.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dzk on 2018/11/16.
 */
@Component
public class RabbitSender {

    // 自动注入RabbitTempalate模板
    @Autowired
    RabbitTemplate rabbitTemplate;

    // publisher-confirms, 实现一个监听器用于监听Broker端给我们返回的确认请求：
    // RabbitTemplate.ComfirmCallBack
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        // ack 返回结果，string 异常信息
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String string) {
            System.err.println("correlationData : " + correlationData);
            System.err.println("ack : " + ack);
            if (!ack) {
                System.err.println("异常处理 。。。");
            }
        }
    };

    //publisher-returns,保证消息对Broker端是可达的，如果出现路由件不可达的情况，
    // 则使用监听器对不可达的消息进行后续的处理，则使用监听器对不可达的消息进行后续的处理，保证消息的路由成功
    //回调函数: return返回
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        // replycode 响应码 replytext 响应信息
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode,
                                    String replyText, String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };

    //发送消息方法调用：构建Message消息
    public void send(Object message, Map<String, Object> properties) {
        // 消息头
        MessageHeaders mhs = new MessageHeaders(properties);
        // 构建消息 org.springframework.messaging
        Message msg = MessageBuilder.createMessage(message, mhs);
        // 设置验证
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 设置return
        rabbitTemplate.setReturnCallback(returnCallback);
        // id + 时间戳 设置时全局唯一（消息补偿）
        CorrelationData correlationData = new CorrelationData("051518");
        // 发送消息
        rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", msg, correlationData);
    }

    //发送消息方法调用: 构建自定义对象消息
    public void sendOrder(Order order) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("0987654321");
        rabbitTemplate.convertAndSend("exchange-2", "springboot.def", order, correlationData);
    }
}
