package com.example.designpattern.Proxy.static_proxy;

public class Proxy implements Subject {
    private Subject subject =null;

    /**
     * 两种构造写法都行
     * @param subject
     */
    public Proxy (Subject subject ){
        this.subject = subject;
    }
    public Proxy (){
        this.subject =new RealSubject();
    }
    @Override
    public void say() {
        subject.say();
    }
}
