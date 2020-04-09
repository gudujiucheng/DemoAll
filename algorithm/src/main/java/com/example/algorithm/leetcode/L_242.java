package com.example.algorithm.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * https://leetcode-cn.com/problems/valid-anagram/
 */
public class L_242 {
    public static void main(String[] args) {

    }
    public static boolean isAnagram(String s, String t) {
        if (s == null || t == null) {
            return false;
        }
        if (s.length() != t.length()) {
            return false;
        }
        if (s.equals(t)) {
            return true;
        }
        HashMap<Character, Integer> map = new HashMap<>();//注意泛型位置，注意 Character 写法
        char[] a = s.toCharArray();//注意api 写法
        char[] b = t.toCharArray();

        for (int i = 0; i < a.length; i++) {//分号
            char key = a[i];
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }
        for (int i = 0; i < b.length; i++) {
            char key = b[i];
            if (map.containsKey(key)) {//api
                int count = map.get(key);
                int nowCount = count - 1;
                if (nowCount < 0) {
                    return false;
                }
                map.put(key, nowCount);
            } else {
                return false;
            }
        }
        for (Character key : map.keySet()) {//遍历api
            int count = map.get(key);
            if (count != 0) {
                return false;
            }

        }
        return true;
    }


    public static boolean isAnagram1(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] str1 = s.toCharArray();
        char[] str2 = t.toCharArray();
        Arrays.sort(str1);//O(nlogn)
        Arrays.sort(str2);
        return Arrays.equals(str1, str2);//复杂度 o(n)

    }
}
