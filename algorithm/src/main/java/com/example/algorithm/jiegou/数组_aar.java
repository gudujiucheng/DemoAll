package com.example.algorithm.jiegou;

import java.util.ArrayList;

public class 数组_aar {
    public static void main(String[] args) {
        //java中要求数组长度是不可变的，是一段连续的内存空间
        String[] aar = new String[5];
        aar[0] = "";


        //如果想变，可以使用集合 比如下面这个集合就是通过数组实现的，当长度不够的时候，就会动态扩容
        ArrayList<String> strings = new ArrayList<>();
        strings.add("");
        //当删除元素的时候  不会主动缩容，可以调用方法实现缩容，不过其实内部也是copy一份
        strings.trimToSize();

    }
}
