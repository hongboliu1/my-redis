package com.ai.redis.model;

/**
 * @author liuhb
 * @date 2019-11-14 16:44
 */
public class MyTestBean implements java.io.Serializable{
    private static final long serialVersionUID = 59634650098518453L;

    private Integer str;

    private String name;

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
