package com.hiram.entity;

import java.io.Serializable;

/**
 * Created by dzk on 2018/11/16.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = -4640485636004664619L;
    
    private String id;

    private String name;

    public Order() {
    }
    public Order(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
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
}
