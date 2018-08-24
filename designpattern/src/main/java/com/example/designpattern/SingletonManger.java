package com.example.designpattern;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例管理类
 */
public class SingletonManger {
    private static Map<String,Object> objectMap = new HashMap<>();

    /**
     * 私有化管理类，防止创建多个
     */
    private SingletonManger(){

    }

    /**
     * 插入单例类
     * @param key
     * @param instance
     */
    public static void registerService(String key,Object instance){
        if(!objectMap.containsKey(key)){
           objectMap.put(key,instance);
        }
    }

    /**
     * 获取单例类
     * @param key
     * @return
     */
    public static Object getSigleInstance(String key){
        return objectMap.get(key);
    }
}
