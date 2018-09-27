package com.example.designpattern.Proxy.static_proxy;

public class RealSubject implements  Subject {

    @Override
    public void say() {
        System.out.print("哈哈哈哈哈哈哈");
    }
}
