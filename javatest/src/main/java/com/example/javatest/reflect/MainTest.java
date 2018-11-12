package com.example.javatest.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MainTest {
    public static void main(String[] args) throws ClassNotFoundException {
//        getMClass();
        getField();
    }




    private static void getField() throws ClassNotFoundException {
        Class class01 =  Class.forName("com.example.javatest.reflect.Dog");
        //获取所有的属性?
        Field[] fs = class01.getDeclaredFields();
        //定义可变长的字符串，用来存储属性
        StringBuffer sb = new StringBuffer();
        //通过追加的方法，将每个属性拼接到此字符串中

        //最外边的public定义
        sb.append(Modifier.toString(class01.getModifiers()) + " class " + class01.getSimpleName() +"{\n");
        //里边的每一个属性
        for(Field field:fs){
            sb.append("\t");//空格
            sb.append(Modifier.toString(field.getModifiers())+" ");//获得属性的修饰符，例如public，static等等
            sb.append(field.getType().getSimpleName() + " ");//属性的类型的名字
            sb.append(field.getName()+";\n");//属性的名字+回车
        }
        sb.append("}");

        System.out.println(sb);


    }


    /**
     * 三种获取类类型的方法
     */
    private static void getMClass() {

        try {
            Class class01 =  Class.forName("com.example.javatest.reflect.Dog");
            System.out.print(class01.getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Class class02 = Dog.class;
        System.out.print(class02.getSimpleName());

        Class class03  =  new Dog().getClass();
        System.out.print(class03.getSimpleName());



        //获取类型之后，可以通过类型创建对象
        try {
            Dog o = (Dog)class02.newInstance();//调用的是无参数的构造方法。
            o.eat();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
