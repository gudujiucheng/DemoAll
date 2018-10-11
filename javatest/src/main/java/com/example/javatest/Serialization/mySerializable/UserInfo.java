package com.example.javatest.Serialization.mySerializable;

import java.io.Serializable;

/**
 * java 序列化方式  使用简单
 */
public class UserInfo  implements Serializable{
    String name;
    int age;
    transient int height;//使用transient关键字修饰的变量不会被序列化
    public  UserInfo(String name,int age,int height){
        this.name = name;
        this.age = age;
        this.height = height;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                '}';
    }
}
