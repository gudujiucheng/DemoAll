package com.example.javatest.class_test;

public class InnerClassTest {
    private String name;
    private int age;


    private void print() {
        System.out.print(name);
    }


    /**
     * 普通内部类(只要不是private 修饰符修饰类，就没问题)
     */
    class Son {
        private String name;

        private Son() {
            //注意同名参数的引用方式
            InnerClassTest.this.name = "哈哈哈哈哈哈";
            age = 11;
        }

        public void test() {
            print();//可以访问外部类的属性或者方法,私有的也是可以访问的
        }

    }


    /**
     * 静态内部类，只能访问外部静态类的静态变量或者方法
     */
    static class StaticSon {
        public void test() {
            System.out.print("静态内部类");
        }

    }

}
