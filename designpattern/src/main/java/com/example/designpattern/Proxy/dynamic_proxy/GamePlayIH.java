package com.example.designpattern.Proxy.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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


    public static void main(String[] args){


        //这一句是生成代理类的class文件，前提是你需要在工程根目录下创建com/sun/proxy目录，不然会报找不到路径的io异常
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

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
