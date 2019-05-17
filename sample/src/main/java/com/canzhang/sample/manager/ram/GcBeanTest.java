package com.canzhang.sample.manager.ram;

import android.util.Log;

/**
 * @Description:
 * @Author: canzhang
 * @CreateDate: 2019/5/17 15:54
 */
public class GcBeanTest {


    private String name;

    public GcBeanTest(String name) {
        this.name = name;
        Log.e("Test","创建了一个对象 name:"+name);
    }

    /**
     * https://www.jianshu.com/p/e1b2db7bafce
     * <p>
     * finalize方法会在垃圾回收器真正回收对象之前调用
     * finalize并不会确保对象会被销毁,它不是析构函数.
     * <p>
     * 垃圾回收内存是JVM的一个不确定操作,通常会在系统濒临内存溢出可能会回收,所以你不要指望垃圾回收来控制finalize方法的调用
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {//这个真的摸不准什么时候调用
        super.finalize();
        Log.e("Test","我销毁了啊，你别拦住我 name:" + this.name);
    }
}
