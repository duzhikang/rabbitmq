package com.hiram.consumer;

import com.hiram.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dzk on 2018/11/16.
 */
@Component
public class RabbitReceiver {

    /**
     *  首先配置手工确认模式，用于ACK的手工处理，这样我们可以保证消息的可靠性送达，
     *  或者再消费端消费失败的时候可以做到重回队列、根据业务记录日志等处理
     *  可以设置消费端的监听个数和最大个数，用于控制消费端的并发情况
     *  @RabbitListener是一个组合注解，里面可以注解配置
     *  @QueueBinding、@Queue、@Exchange直接通过这个组合注解一次性搞定消费端交换机。队列、绑定、路由、并且配置监听功能等
     *  由于类配置写在代码里非常不友好、所以强烈建议大家使用配置文件配置
     */
    /*@RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-2", durable = "true"),
            exchange = @Exchange(value = "exchange-1",
            durable = "true",
            type = "topic",
            ignoreDeclarationExceptions = "true"),
            key = "springboot.#"
    ))*/
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable="${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable="${spring.rabbitmq.listener.order.exchange.durable}",
                    type= "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.key}"
    )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               Channel channel,
                               @Headers Map<String, Object> headers) throws Exception {
        // @Payload 代表着消息体
        System.err.println("--------------------------------------");
        System.err.println("消费端order: " + order.getId());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag, false);
    }
    /*public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("__________________________________");
       // System.out.println(message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ack
        channel.basicAck(deliveryTag, false);
    }*/
    /**
     ** Basic.Ack 发回给 RabbitMQ 以告知，可以将相应 message 从 RabbitMQ 的消息缓存中移除。
     *Basic.Ack 未被 consumer 发回给 RabbitMQ 前出现了异常，RabbitMQ 发现与该 consumer 对应的连接被断开，之后将该 message 以轮询方式发送给其他 consumer （假设存在多个 consumer 订阅同一个 queue）。
     *在 no_ack=true 的情况下，RabbitMQ 认为 message 一旦被 deliver 出去了，就已被确认了，所以会立即将缓存中的 message 删除。所以在 consumer 异常时会导致消息丢失。
     *来自 consumer 侧的 Basic.Ack 与 发送给 Producer 侧的 Basic.Ack 没有直接关系。

     */

    /*spring.rabbitmq.listener.order.queue.name=queue-2
    spring.rabbitmq.listener.order.queue.durable=true
    spring.rabbitmq.listener.order.exchange.name=exchange-2
    spring.rabbitmq.listener.order.exchange.durable=true
    spring.rabbitmq.listener.order.exchange.type=topic
    spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions=true
    spring.rabbitmq.listener.order.key=springboot.**/
}
