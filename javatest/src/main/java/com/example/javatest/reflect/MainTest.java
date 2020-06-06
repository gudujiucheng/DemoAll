package com.example.javatest.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        getMClass();
//        getField();
//        getFieldValue();
        setFieldValue();
//        getCon();
//        getMethod();
    }

    private static void getMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1.加载Class对象
        Class stuClass = Class.forName("com.example.javatest.reflect.Dog");

        //2.获取所有公有方法
        System.out.println("***************获取所有的”公有“方法(包括父类公有方法)*******************");
        stuClass.getMethods();
        Method[] methodArray = stuClass.getMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }
        System.out.println("***************获取所有的方法，包括私有的*******************");
        methodArray = stuClass.getDeclaredMethods();
        for(Method m : methodArray){
            System.out.println(m);
        }
        System.out.println("***************获取公有的show1()方法*******************");
        Method m = stuClass.getMethod("show1", String.class);
        System.out.println(m);
        //实例化一个Student对象
        Object obj = stuClass.getConstructor().newInstance();
        m.invoke(obj, "哈哈哈哈啊哈");

        System.out.println("***************获取私有的show4()方法******************");
        m = stuClass.getDeclaredMethod("show4", int.class);
        System.out.println(m);
        m.setAccessible(true);//解除私有限定
        Object result = m.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
        System.out.println("返回值：" + result);

    }

    private static void getCon() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1.加载Class对象
        Class clazz = Class.forName("com.example.javatest.reflect.Dog");


        //2.获取所有公有构造方法
        System.out.println("**********************所有公有构造方法*********************************");
        Constructor[] conArray = clazz.getConstructors();

        for(Constructor c : conArray){
            System.out.println(c);
        }


        System.out.println("************所有的构造方法(包括：私有、受保护、默认、公有)***************");
        conArray = clazz.getDeclaredConstructors();
        for(Constructor c : conArray){
            System.out.println(c);
        }

//        System.out.println("*****************获取公有、无参的构造方法*******************************");
//        Constructor con = clazz.getConstructor(null);
//        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
//        //2>、返回的是描述这个无参构造函数的类对象。
//
//        System.out.println("con = " + con);
//        //调用构造方法
//        Object obj = con.newInstance();
        //	System.out.println("obj = " + obj);
        //	Student stu = (Student)obj;

//        System.out.println("******************获取私有构造方法，并调用*******************************");
//        con = clazz.getDeclaredConstructor(char.class);
//        System.out.println(con);
//        //调用构造方法
//        con.setAccessible(true);//暴力访问(忽略掉访问修饰符)
//        obj = con.newInstance('旺');
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



        Field age = c.getDeclaredField("age");//final 类型
        //打破封装（针对私有变量，不提前声明这个直接操作变量的话会报错）
        age.setAccessible(true); //使用反射机制可以打破封装性，导致了java对象的属性不安全。
        //给属性赋值
        age.set(o, 123); //set
        //get
        System.out.println("测试final 是否可以改："+age.get(o));


        Field desc = c.getDeclaredField("desc");//final 类型
        //打破封装（针对私有变量，不提前声明这个直接操作变量的话会报错）
        desc.setAccessible(true); //使用反射机制可以打破封装性，导致了java对象的属性不安全。
        //给属性赋值
        desc.set(o, "desc改成功了"); //set
        //get
        System.out.println("测试final 是否可以改："+desc.get(o));

    }


    private static void getField() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class stuClass =  Class.forName("com.example.javatest.reflect.Dog");


       //2.获取字段
        System.out.println("************获取所有公有的字段(包括父类公有字段)********************");
        Field[] fieldArray = stuClass.getFields();
        for(Field f : fieldArray){
            System.out.println(f);
        }
        System.out.println("************获取所有的字段(包括私有、受保护、默认的)********************");
        fieldArray = stuClass.getDeclaredFields();
        for(Field f : fieldArray){
            System.out.println(f);
        }
        System.out.println("*************获取公有字段**并调用***********************************");
        Field f = stuClass.getField("height");
        System.out.println(f);
        //获取一个对象
        Object obj = stuClass.getConstructor().newInstance();
        //为字段设置值
        f.set(obj, 111);//为对象中的height属性赋值--》stu.height = 111
        //验证
        Dog stu = (Dog)obj;
        System.out.println("验证height：" + stu.height);


        System.out.println("**************获取私有字段****并调用********************************");
        f = stuClass.getDeclaredField("weight");
        System.out.println(f);
        f.setAccessible(true);//暴力反射，解除私有限定
        f.set(obj, 188);
        System.out.println("验证weight：" + stu);




    }

    //反射获取对象成员变量的值
    private static void getFieldValue() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class stuClass =  Class.forName("com.example.javatest.reflect.Dog");

        //获取一个对象
        Object obj = stuClass.getConstructor().newInstance();
        Dog stu = (Dog)obj;
        stu.height = 100;

        System.out.println("**************获取公有&私有字段的值********************************");

        Field weightField = stuClass.getDeclaredField("weight"); //私有的
        System.out.println(weightField);
        weightField.setAccessible(true);//暴力反射，解除私有限定
        Object fieldValue = weightField.get(stu);
        System.out.println("验证weight：" + fieldValue);


        Field heightField = stuClass.getDeclaredField("height");//公开的
        System.out.println(heightField);
        Object heightFieldValue = heightField.get(stu);
        System.out.println("验证height：" + heightFieldValue);

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
//        Class class03  =  new Dog().getClass();
//        System.out.print(class03.getSimpleName());



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
