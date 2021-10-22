package com.fengx.bootlog.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @author: Fengx
 * @date: 2021-09-26
 * @description: 用户
 **/
// 加上了@Document注解之后，默认情况下这个实体中所有的属性都会被建立索引、并且分词
// indexName 索引名; type 类型名（7.x版本以上已废除）
@Document(indexName = "demo")
public class User {

    @Id
    private String id;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
    // 级别
    private String level;
    // 时间
    private Date entryDate;
    // 电话
    private String mobile;
    // 邮箱
    private String email;
    // 地址
    private String address;


    public User(String id, String name, Integer age, String level, Date entryDate, String mobile, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.level = level;
        this.entryDate = entryDate;
        this.mobile = mobile;
        this.email = email;
        this.address = address;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
