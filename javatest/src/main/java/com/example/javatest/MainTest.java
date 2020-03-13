package com.example.javatest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine().trim();
        ArrayList<Integer> list = new ArrayList<>();
        String[] arr = input.split(",");
        for (int i = 0; i < arr.length; i++) {
            list.add(Integer.parseInt(arr[i]));
        }
        //我想问测试用例1，为什么返回true？1,8,6出现一次，2,3,出现两次，为什么返回是true
        if (fun1(list)) {
            System.out.println("true");
        }
        if (fun2(list)) {
            System.out.println("false");
        } else {
            System.out.println("true");
        }

    }

    public static boolean fun1(ArrayList<Integer> list) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (map.containsKey(list.get(i))) {
                map.put(list.get(i), map.get(list.get(i)) + 1);
            } else {
                map.put(list.get(i), 1);
            }
        }
        for (Integer key : map.keySet()) {
            if (map.get(key) < 2) {
                return false;
            }
        }
        return true;
    }

    public static boolean fun2(ArrayList<Integer> list) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            if (set.contains(list.get(i))) {
                return false;
            }
        }
        return true;
    }


}
