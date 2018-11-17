# rabbitmq序列化问题

- 不同的服务中，实体类的绝对路径必须一样，序列化id也必须一样

# spring cloud stream 问题

- `@Output(Barista.OUTPUT_CHANNEL)，`这个注解不仅生成一个信道，还可以生成一个交换机，消息会发送到注解生成的交换机里面。
- spring.cloud.stream.bindings.input_channel.destination=output_channel 和 `Barista.OUTPUT_CHANNEL`要保持一致，否则会出错

