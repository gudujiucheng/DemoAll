package com.example.designpattern.Proxy.static_proxy;

public class MainProxy {
    public static void main(String[] args){
        new Proxy(new RealSubject()).say();
        new Proxy().say();
    }
}
