package com.example.administrator.demoall.bean.xuliehua;

import java.io.Serializable;

/**
 * 测试序列化的传递
 */
public class SerializeBean implements Serializable {
    String name;
    int age;
    public  SerializeBean(String name,int age){
        this.name = name;
        this.age = age;
    }
}
