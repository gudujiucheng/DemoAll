package com.canzhang.sample.manager.asm.slow_method;

import android.support.annotation.Keep;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 慢方法采集提示
 * @Author: canzhang
 * @CreateDate: 2019/10/9 11:38
 */
public class SlowMethodHelper {


    public static Map<String, Long> sStartTime = new HashMap<>();
    public static Map<String, Long> sEndTime = new HashMap<>();

    @Keep
    public static void setStartTime(String methodName, long time) {
        sStartTime.put(methodName, time);
    }

    @Keep
    public static void setEndTime(String methodName, long time) {
        sEndTime.put(methodName, time);
    }

    @Keep
    public static String getCostTime(String methodName) {
        long start = sStartTime.get(methodName);
        long end = sEndTime.get(methodName);
        return "method: " + methodName + " main " + (end - start) + " ns";
    }


}
