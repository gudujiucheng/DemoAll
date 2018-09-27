package com.example.designpattern.Proxy.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MainDynamicProxy {
    public static void main(String[] args){
        //玩家
        IGamePlayer player = new GamePlayer();
        //动态代理类
        InvocationHandler handler = new GamePlayIH(player);
        ClassLoader classLoader = player.getClass().getClassLoader();
        Class<?>[] interfaces = player.getClass().getInterfaces();

        //动态产生一个代理者
        IGamePlayer gamePlayerProxy = (IGamePlayer) Proxy.newProxyInstance(classLoader,interfaces,handler);

        gamePlayerProxy.playGame();



    }
}
