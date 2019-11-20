package com.canzhang.sample.manager.jni;

/**
 * @Description: cmake方式：https://blog.csdn.net/u013564742/article/details/86512271
 * @Author: canzhang
 * @CreateDate: 2019/11/20 11:20
 */
public class JNICmake {
    // 动态导入 so 库
    static {
        System.loadLibrary("native-lib");
    }
    public native static String stringFromJNI();
}
