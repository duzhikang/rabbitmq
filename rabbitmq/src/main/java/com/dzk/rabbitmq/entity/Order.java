package com.dzk.rabbitmq.entity;

import java.io.Serializable;

/**
 * Created by dzk on 2018/11/16.
 */
public class Order implements Serializable{

    private static final long serialVersionUID = -3578111817813656464L;

    private String id;

    private String name;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
