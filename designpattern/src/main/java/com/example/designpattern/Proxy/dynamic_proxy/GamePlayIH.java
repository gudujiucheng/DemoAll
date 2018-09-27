package com.example.designpattern.Proxy.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类
 */
public class GamePlayIH implements InvocationHandler{
    private Object object;

    public GamePlayIH(Object object){
        this.object = object;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equalsIgnoreCase("playGame")){
            System.out.print("想通过我代理类玩游戏，必须打印这句话"+"\n");
        }
        Object objectResult = method.invoke(this.object,args);
        if(method.getName().equalsIgnoreCase("playGame")){
            System.out.print("\n"+"玩完了得告诉我");
        }
        return objectResult;
    }
}
