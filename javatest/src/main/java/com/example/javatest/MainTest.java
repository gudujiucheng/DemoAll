package com.example.javatest;

public class MainTest {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
// TODO        没有静态(static)的类中类不能使用外部类进行.操作,必须用实例来进行实例化类中类.
//        内部类测试.Son son =  new 内部类测试.Son();
        内部类测试 nei = new 内部类测试();
//        内部类测试.Son son  =  nei.new Son();
//        son.test();

        //静态内部类这样创建
        内部类测试.StaticSon staticSon =  new 内部类测试.StaticSon();
        staticSon.test();

    }
}
