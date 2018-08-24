package com.example.algorithm.demo;


public class MainTest {
    public static void main(String[] args) {
        test();

    }


    private static void test(){
        int[] list = new int[]{5,2,6,6,1,9,4};
//        选择排序_.select02(list);
        冒泡排序_.bubble(list);
    }


    /**
     * 打印集合
     * @param list
     */
    public  static void printList(int[] list) {
        for (int i = 0; i <list.length ; i++) {
            System.out.println( list[i]);
        }
    }



}
