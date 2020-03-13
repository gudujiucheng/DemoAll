package com.example.algorithm.test;

public class MainTest {

    public static void main(String[] args) {
        test02();
    }


    /**
     * 自己想的排序1  其实是选择排序
     */
    private static void test() {
        int[] list = new int[]{5, 2, 6, 6, 1, 9, 4};
        for (int i = 0; i < list.length; i++) {
            int temp = list[i];//首先想到要取出第一个元素
            int index = i;//然后想到如果把最小的那个元素放到第一位，后续就可以从i+1位置开始比较了，继续找最小的，所以需要记录一个索引，比较完毕好替换。
            for (int j = i; j < list.length; j++) {//i和 i前面的都比较过了，那就从i开始
                if (temp > list[j]) {//比较大小记录最小的索引值
                    temp = list[j];
                    index = j;
                }
            }
            if (index != i) {//根据记录最小的索引，进行值的互换
                int a = list[i];
                list[i] = list[index];
                list[index] = a;
            }
        }
        printList(list);
    }
    /**
     * 自己想的排序2  其实是冒泡排序
     */
    private static void test02(){
        int[] list = new int[]{5, 2, 6, 6, 1, 9, 4};
        for (int j =list.length; j >0 ; j--) {//2、最大的取出来过了，每次循环的长度要减去1，还从头开始循环互换
            for (int i = 0; i <j-1; i++) {
                if(list[i]>list[i+1]){//1、互换实现把最大的放大最后面
                    int temp =  list[i];
                    list[i] = list[i+1];
                    list[i+1] = temp;
                }
            }
        }
        printList(list);
    }

    private static void test03(){
        int[] list = new int[]{5, 2, 6, 6, 1, 9, 4};
        int len = list.length;
        for (int i = 0; i <len ; i++) {
            int base = list[0];//取个基准数字



        }


        printList(list);
    }


    /**
     * 打印集合
     *
     * @param list
     */
    public static void printList(int[] list) {
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }


}
