package com.heima.freemarker.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Student {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private int age;
    /**
     * 出生年月
     */
    private Date birthday;
    /**
     * 钱包
     */
    private Float money;
}