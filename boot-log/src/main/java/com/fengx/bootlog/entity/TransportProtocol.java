package com.fengx.bootlog.entity;

/**
 * @author: Fengx
 * @date: 2021-09-26
 * @description: 传输对象
 **/

public class TransportProtocol {

    /**
     * 用户信息
     */
    private Integer type;
    /**
     * 传输对象
     */
    private Object obj;

    public TransportProtocol() {
    }

    public TransportProtocol(Integer type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
