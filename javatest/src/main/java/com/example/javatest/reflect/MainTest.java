package com.example.javatest.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MainTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InstantiationException {
//        getMClass();
//        getField();
        setFieldValue();
    }

    private static void setFieldValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class c =  Class.forName("com.example.javatest.reflect.Dog");

//        Field f = c.getDeclaredField("height");//public
        Object o = c.newInstance();

        Field f = c.getDeclaredField("weight");//私有成员变量

        //打破封装（针对私有变量，不提前声明这个直接操作变量的话会报错）
        f.setAccessible(true); //使用反射机制可以打破封装性，导致了java对象的属性不安全。
        //给属性赋值
        f.set(o, 123); //set
        //get
        System.out.println(f.get(o));


    }


    private static void getField() throws ClassNotFoundException {
        Class class01 =  Class.forName("com.example.javatest.reflect.Dog");


//        getField和getDeclaredField都是Class类的方法，反射成员变量时使用。(getMethod和getDeclaredMethod类似)
//
//        getField:获取一个类的 ==public成员变量，包括基类== 。

//        getDeclaredField:获取一个类的 ==所有成员变量，不包括基类== 。



        Field[] fs = class01.getDeclaredFields();
//        Field[] fs = class01.getFields();

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

        //1、全类名

        try {
            Class class01 =  Class.forName("com.example.javatest.reflect.Dog");
            System.out.print(class01.getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //2、类名
        Class class02 = Dog.class;
        System.out.print(class02.getSimpleName());

        //3、对象
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
