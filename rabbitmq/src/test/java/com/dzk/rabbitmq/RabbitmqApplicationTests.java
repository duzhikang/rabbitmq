package com.dzk.rabbitmq;

import com.dzk.rabbitmq.config.RabbitMqFastJsonClassMapper;
import com.dzk.rabbitmq.entity.Order;
import com.dzk.rabbitmq.entity.Packaged;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

	@Autowired
	RabbitAdmin rabbitAdmin;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Test
	public void testAdmin() {
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));

		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));

		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));

		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));

		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

		rabbitAdmin.declareBinding(new Binding("test.direct.queue",
				Binding.DestinationType.QUEUE,
				"test.direct", "direct", new HashMap<>()));

		rabbitAdmin.declareBinding(
				BindingBuilder
						.bind(new Queue("test.topic.queue", false))		//直接创建队列
						.to(new TopicExchange("test.topic", false, false))	//直接创建交换机 建立关联关系
						.with("user.#"));	//指定路由Key


		rabbitAdmin.declareBinding(
				BindingBuilder
						.bind(new Queue("test.fanout.queue", false))
						.to(new FanoutExchange("test.fanout", false, false)));

		//清空队列数据
		rabbitAdmin.purgeQueue("test.topic.queue", false);


	}


	@Test
	public void testSendMessage() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("desc", "信息描述..");
		messageProperties.getHeaders().put("type", "自定义消息类型..");
		Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);

		rabbitTemplate.convertAndSend("test.topic", "user.send1", message, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				System.err.println("------添加额外的设置---------");
				message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
				message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
				return message;
			}
		});
	}

	@Test
	public void testSendMessage2() throws Exception {
		//1 创建消息属性
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		// 创建消息
		Message message = new Message("mq 消息1234".getBytes(), messageProperties);

		// 发送消息message
		rabbitTemplate.send("test.topic", "user.abc", message);
        // 发送消息object
		rabbitTemplate.convertAndSend("test.topic", "user.send1", "hello object message send! routing key:user.send1");
		rabbitTemplate.convertAndSend("test.topic", "user.send2", "hello object message send! routing key:user.send2");

		rabbitTemplate.convertAndSend("test.topic", "user.object", "don't have messageproperties");
	}

	@Test
	public void testSendMessage4Text() throws Exception {
		//1 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("mq 消息1234".getBytes(), messageProperties);

		rabbitTemplate.send("topic001", "spring.abc", message);
		rabbitTemplate.send("topic002", "rabbit.abc", message);
	}

	@Test
	public void testSendJsonMessage() throws Exception {

		Order order = new Order();
		order.setId("001");
		order.setName("消息订单");
		order.setContent("描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);

		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);

		rabbitTemplate.send("topic001", "spring.order", message);
	}

	@Test
	public void testSendMappingMessage() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
        // 转为json
		String json1 = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json1);

		MessageProperties messageProperties1 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties1.setContentType("application/json");
		messageProperties1.getHeaders().put("__TypeId__", "order");
		//ClassMapper classMapper = new RabbitMqFastJsonClassMapper();
		//classMapper.fromClass(order.getClass(), messageProperties1);
		Message message1 = new Message(json1.getBytes(), messageProperties1);
		rabbitTemplate.send("topic001", "spring.order", message1);

		Packaged pack = new Packaged();
		pack.setId("002");
		pack.setName("包裹消息");
		pack.setDescription("包裹描述信息");

		String json2 = mapper.writeValueAsString(pack);
		System.err.println("pack 4 json: " + json2);

		MessageProperties messageProperties2 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties2.setContentType("application/json");
		messageProperties2.getHeaders().put("__TypeId__", "packaged");
		//classMapper.fromClass(pack.getClass(), messageProperties2);
		Message message2 = new Message(json2.getBytes(), messageProperties2);
		rabbitTemplate.send("topic001", "spring.pack", message2);
	}


}
