package com.example.designpattern.single.better;

/**
 * 懒汉式
 */
public class BetterSingle {



    private BetterSingle(){

    }
    private static class SingleHolder{
        //静态的不会被构造多次
        private static final  BetterSingle single = new BetterSingle();
    }

    public static BetterSingle getInstance(){
        //只有当用到的时候 才会创建加载
        return  SingleHolder.single;
    }
}
