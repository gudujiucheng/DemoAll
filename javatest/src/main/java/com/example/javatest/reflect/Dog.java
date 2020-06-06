package com.example.javatest.reflect;

public class Dog  extends  Animal{
    public int height;
    private int weight = -1;
    final int age = 100;//测试final 变量是否可以更改
    private final String  desc = "xxxx";//测试final 字符串 变量是否可以更改



        //---------------构造方法-------------------
        //（默认的构造方法）
        Dog(String str){
            System.out.println("(默认)的构造方法 s = " + str);
        }

        //无参构造方法
        public Dog(){
            System.out.println("调用了公有、无参构造方法执行了。。。");
        }

        //有一个参数的构造方法
        public Dog(char name){
            System.out.println("姓名：" + name);
        }

        //有多个参数的构造方法
        public Dog(String name ,int age){
            System.out.println("身高："+height+" 重量："+ weight);//这的执行效率有问题，以后解决。
        }

        //受保护的构造方法
        protected Dog(boolean n){
            System.out.println("受保护的构造方法 n = " + n);
        }

        //私有构造方法
        private Dog(int age){
            System.out.println("私有的构造方法   身高："+ height);
        }

    @Override
    public String toString() {
        return "Dog{" +
                "height=" + height +
                ", weight=" + weight +
                ", sex=" + sex +
                '}';
    }


    //**************成员方法***************//
    public void show1(String s){
        System.out.println("调用了：公有的，String参数的show1(): s = " + s);
    }
    protected void show2(){
        System.out.println("调用了：受保护的，无参的show2()");
    }
    void show3(){
        System.out.println("调用了：默认的，无参的show3()");
    }
    private String show4(int age){
        System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
        return "xxxxx";
    }

}
