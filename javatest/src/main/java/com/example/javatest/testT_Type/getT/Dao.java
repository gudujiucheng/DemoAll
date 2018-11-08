package com.example.javatest.testT_Type.getT;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 父类里面有泛型
 *
 * @param <T> 我们在Dao层对数据库进行操作常常需要得到的是实体的class类，
 *            也就是上面的Student这个实体类，(SubDao)
 *            我们得到其class实例之后就可以利用反射获取到里面的实体类中各种方法和变量，进而我们可以为实体类进行操作
 *            <p>
 *            原文：https://blog.csdn.net/datouniao1/article/details/53788018
 */
public class Dao<T,K,W> {
    public Dao() {
        System.out.print("调用处类型是：" + this.getClass().getSimpleName());
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = null;
        if (superclass instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) superclass;
            Type[] typeArray = parameterizedType.getActualTypeArguments();
            if (typeArray != null && typeArray.length > 0) {
                Class clazz = (Class) typeArray[0];

                System.out.print("\n类型是：" + clazz.getSimpleName());


                for (int i = 0; i <typeArray.length ; i++) {
                    System.out.print("\n遍历类型第"+i+"个是：" + ((Class) typeArray[i]).getSimpleName());
                }

            }
        }

    }
}
