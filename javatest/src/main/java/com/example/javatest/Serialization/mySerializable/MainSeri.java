package com.example.javatest.Serialization.mySerializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainSeri {

    private static String  fileName = "/Users/zhangcan603/AndroidStudioProjects/DemoAll/javatest"+ File.separator + "userInfo.ser";
    public static void main(String[] args) throws IOException,ClassNotFoundException {
//        serializable();
        unSerializable();

    }

    /**
     * 反序列化
     */
    private static void unSerializable() throws IOException, ClassNotFoundException {
        ObjectInputStream in=new ObjectInputStream(new FileInputStream(fileName));

        UserInfo userInfo=(UserInfo) in.readObject();//会员对象

        System.out.println(userInfo.toString());

    }

    /**
     * 序列化
     * @throws IOException
     */
    private static void serializable() throws IOException {
        File file = new File(fileName);
        ObjectOutputStream oos = null ;
        //装饰流（流）
        oos = new ObjectOutputStream(new FileOutputStream(file)) ;
        //实例化类
        UserInfo per = new UserInfo("张灿",30,175) ;
        oos.writeObject(per) ;
        //把类对象序列化
        oos.close() ;
    }




}
