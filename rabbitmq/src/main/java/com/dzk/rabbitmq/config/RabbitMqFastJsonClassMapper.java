package com.dzk.rabbitmq.config;

import org.springframework.amqp.support.converter.DefaultClassMapper;

/**
 * 在SpringBoot 2.0.0版本时，DefaultClassMapper类源码构造函数进行了修改，
 * 不再信任全部package而是仅仅信任java.util、java.lang
 * Created by dzk on 2018/11/16.
 */
public class RabbitMqFastJsonClassMapper extends DefaultClassMapper {

    /**
     * 构造函数初始化信任所有pakcage
     */
    public RabbitMqFastJsonClassMapper() {
        super();
        setTrustedPackages("*");
    }
}
