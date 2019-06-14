package com.example.javatest.class_test.extend_test;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/6/14 10:11
 */
public class Child extends Parent {
    public Child() {
        System.out.print("\ni am from child current is:" + getClass().getSimpleName());
    }
}
